package com.lm2a.tacoonline.api;


import com.lm2a.tacoonline.data.TacoRepository;
import com.lm2a.tacoonline.exceptions.TacoNotFoundException;
import com.lm2a.tacoonline.model.Taco;
import lombok.RequiredArgsConstructor;
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
@CrossOrigin(origins="*")
@RequiredArgsConstructor
@RequestMapping("/api/taco")
public class ApiTacoController {

    private final TacoRepository tacoRepository;

//    @GetMapping("/{id}")
//    public Taco getTacoById(@PathVariable("id") long id) {
//        Optional<Taco> optTaco = tacoRepository.findById(id);
//        if (optTaco.isPresent()) {
//            return optTaco.get();
//        }
//        return null;
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> getTacoById(@PathVariable("id") long id) {
        Optional<Taco> optTaco = tacoRepository.findById(id);
        if (optTaco.isPresent()) {
            return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Taco> postTaco(@RequestBody Taco taco) {
        Taco saved = tacoRepository.save(taco);
        if(saved != null){
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<Iterable<Taco>> getAllTacos(){
        Iterable<Taco> tacos = tacoRepository.findAll();
        return new ResponseEntity<>(tacos, HttpStatus.OK);
    }

    @GetMapping("/recents/{page}")
    public ResponseEntity<Iterable<Taco>> getAllTacos(@PathVariable("page") int page) {
        Pageable pageable = PageRequest.of(page,3, Sort.by("createdAt").descending());
        Iterable<Taco> tacos = tacoRepository.findAll(pageable);
        return new ResponseEntity<>(tacos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Taco> deleteTaco(@PathVariable("id") Long id) {
        Optional<Taco> optTaco = tacoRepository.findById(id);
        if(optTaco.isPresent()) {
            tacoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PutMapping
    public ResponseEntity<Taco> updateTaco(@RequestBody Taco taco)
    {
        Taco savedTaco = tacoRepository.findById(taco.getId()).orElseThrow(() -> new TacoNotFoundException());
        Taco tacoUpdated = tacoRepository.save(taco);
        return new ResponseEntity<>(tacoUpdated, HttpStatus.OK);
    }

}
