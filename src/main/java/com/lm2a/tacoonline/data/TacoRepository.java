package com.lm2a.tacoonline.data;

import com.lm2a.tacoonline.model.Taco;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TacoRepository extends PagingAndSortingRepository<Taco,Long>, CrudRepository<Taco,Long> {
}
