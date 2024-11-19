package com.example.clipdownloader1.repo;

import com.example.clipdownloader1.entity.Sport;
import org.springframework.data.repository.CrudRepository;

/**redis test용 repo*/
public interface SportRedisRepo extends CrudRepository<Sport, Long> {

}
