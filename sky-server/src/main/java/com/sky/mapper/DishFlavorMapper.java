package com.sky.mapper;

import com.sky.entity.DishFlavor;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishFlavorMapper {


  void insertBatch(List<DishFlavor> flavors);

  @Delete("delete from dish_flavor where dish_id = #{dishId}")
  void deleteByDishId(Long dishId);

  void deleteByDishIds(List<Long> dishIds);

  @Select("select * from dish_flavor where dish_id = #{dishId}")
  List<DishFlavor> getByDishId(Long dishId);


}
