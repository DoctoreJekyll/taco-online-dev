package com.lm2a.tacoonline.data;

import com.lm2a.tacoonline.model.Taco;
import org.springframework.data.repository.CrudRepository;

public interface TacoRepository extends CrudRepository<Taco, String> {
}
