package com.sky.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealDishMapper {
  List<Long> getSetmealIdByDishIds(List<Long> dishIds);

}
