package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {

  @Autowired
  private SetMealService setmealService;

  /**
   * 新增套餐
   * @param setmealDTO
   * @return
   */
  @PostMapping
  @ApiOperation("新增套餐")
  @CacheEvict(cacheNames = "setmealNames", key = "#setmealDTO.categoryId")
  public Result save(@RequestBody SetmealDTO setmealDTO) {
    setmealService.saveWithDish(setmealDTO);
    return Result.success();
  }

  @GetMapping("/page")
  @ApiOperation("分页查询")
  public Result<PageResult> page(
      SetmealPageQueryDTO setmealPageQueryDTO) {
    PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
    return Result.success(pageResult);
  }

  @DeleteMapping
  @ApiOperation("批量删除套餐")
  @CacheEvict(cacheNames = "setmealCache", allEntries = true)
  public Result delete(@RequestParam List<Long> ids){
    setmealService.deleteBatch(ids);
    return Result.success();
  }

  @GetMapping("/{id}")
  @ApiOperation("根据id查询套餐")
  public Result<SetmealVO> getById(@PathVariable Long id) {
    SetmealVO setmealVO = setmealService.getByIdWithDish(id);
    return Result.success(setmealVO);
  }

  @PutMapping
  @ApiOperation("修改套餐")
  @CacheEvict(cacheNames = "setmealCache", allEntries = true)
  public Result update(@RequestBody SetmealDTO setmealDTO) {
    setmealService.update(setmealDTO);
    return Result.success();
  }

  @PostMapping("/status/{status}")
  @ApiOperation("套餐起售停售")
  @CacheEvict(cacheNames = "setmealCache", allEntries = true)
  public Result startOrStop(@PathVariable Integer status, Long id) {
    setmealService.startOrStop(status, id);
    return Result.success();
  }
}
