package com.lm2a.tacoonline.api;

import com.lm2a.tacoonline.data.TacoRepository;
import com.lm2a.tacoonline.model.Taco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/taco")
public class ApiTacoController {

    TacoRepository tacoRepository;

    public ApiTacoController(TacoRepository tacoRepository) {
        this.tacoRepository = tacoRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> getTacoById(@PathVariable("id") long id)
    {
        Optional<Taco> taco = tacoRepository.findById(id);
        if (taco.isPresent()) {
            return new ResponseEntity<>(taco.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/list")
    public ResponseEntity<Iterable<Taco>> getAllTaco()
    {
        Iterable<Taco> tacos = tacoRepository.findAll();
        return new ResponseEntity<>(tacos, HttpStatus.OK);
    }

    @GetMapping("/recents/{page}")
    public ResponseEntity<Iterable<Taco>> getAllTacoRecents(@PathVariable("page") int page)
    {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        Iterable<Taco> tacos = tacoRepository.findAll(pageable);
        return new ResponseEntity<>(tacos, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Taco> postTaco(@RequestBody Taco taco)
    {
        Taco saved = tacoRepository.save(taco);
        if (saved != null) {
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Taco> deleteTacoById(@PathVariable("id") long id)
    {
        Optional<Taco> taco = tacoRepository.findById(id);
        if (taco.isPresent()) {
            tacoRepository.delete(taco.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping()
    public ResponseEntity<Taco> updateTaco(@RequestBody Taco taco)
    {
        Optional<Taco> tacoOptional = tacoRepository.findById(taco.getId());
        if (tacoOptional.isPresent()) {
            Taco tacoToUpdate = tacoOptional.get();
            tacoToUpdate.setName(taco.getName());
            tacoToUpdate.setIngredients(taco.getIngredients());

            tacoRepository.save(tacoToUpdate);
            return new ResponseEntity<>(tacoToUpdate, HttpStatus.OK);
        }

        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
