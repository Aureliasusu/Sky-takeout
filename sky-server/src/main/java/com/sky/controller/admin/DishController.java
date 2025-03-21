package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
  @Autowired
  private RedisTemplate redisTemplate;
  @Autowired
  private DishService dishService;
  @PostMapping
  @ApiOperation("新增菜品")
  public Result save(@RequestBody DishDTO dishDTO){
    log.info("新增菜品：{}", dishDTO);
    dishService.saveWithFlavor(dishDTO);
    String key = "dish_" + dishDTO.getCategoryId();
    redisTemplate.delete(key);
    return Result.success();
  }
  @GetMapping("/page")
  @ApiOperation("菜品分页查询")
  public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
    log.info("菜品分页查询：{}", dishPageQueryDTO);
    PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
    return Result.success(pageResult);
  }

  @DeleteMapping
  @ApiOperation("菜品批量删除")
  public Result delete(@RequestParam List<Long> ids){
    log.info("菜品批量删除：{}", ids);
    dishService.deleteBatch(ids);
    Set keys = redisTemplate.keys("dish*");
    redisTemplate.delete(keys);
    return Result.success();
  }

  @GetMapping("/{id}")
  @ApiOperation("根据id查询菜品")
  public Result<DishVO> getById(@PathVariable Long id){
    log.info("根据id查询菜品：{}", id);
    DishVO dishVO = dishService.getByIdWithFlavor(id);
    return Result.success(dishVO);
  }

  @PutMapping
  @ApiOperation("修改菜品")
  public Result update(@RequestBody DishDTO dishDTO){
    log.info("修改菜品：{}", dishDTO);
    dishService.updateWithFlavor(dishDTO);
    cleanCache("dish*");
    return Result.success();
  }



  @GetMapping("/list")
  @ApiOperation("根据分类id查询菜品")
  public Result<List<Dish>> list(Long categoryId){
    List<Dish> list = dishService.list(categoryId);
    return Result.success(list);
  }

  private void cleanCache(String pattern){
    Set keys = redisTemplate.keys(pattern);
    redisTemplate.delete(keys);
  }
  @PostMapping("/status/{status}")
  @ApiOperation("菜品起售停售")
  public Result<String> startOrStop(@PathVariable Integer status, Long id){
    dishService.startOrStop(status,id);
    cleanCache("dish*");
    return Result.success();
  }

}
