package [package].controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import [package].pojo.[Table2];
import [package].service.[Table2]Service;
import entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * [comment] 控制器层
 * @author [author]
 */
@Slf4j
@RestController
@RequestMapping("[table2]")
public class [Table2]Controller {
	@Autowired
	private [Table2]Service [table2]Service;

	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public Result<List<[Table2]>> findAll(){
		return Result.success([table2]Service.list());
	}

	/**
	 * 根据ID查询
	 * @param [key2]
	 * @return
	 */
	@GetMapping("{[key2]}")
	public Result<[Table2]> findById(@PathVariable String [key2]){
		return Result.success([table2]Service.getById([key2]));
	}

	/**
	 * 分页
	 * @param currentIndex 页码
	 * @param pageSize 页大小
	 * @return 分页结果
	 */
	@GetMapping("getList/{currentIndex}/{pageSize}")
	public Result<IPage<[Table2]>> getList(@PathVariable int currentIndex, @PathVariable int pageSize) {
		IPage<[Table2]> list = [table2]Service.getList(currentIndex,pageSize);
		return Result.success(list);
	}

	/**
	 * 分页+多条件查询
	 * @param currentIndex 页码
	 * @param pageSize 页大小
     * @param searchMap 查询条件封装
	 * @return 分页结果
	 */
	@PostMapping("search/{currentIndex}/{pageSize}")
	public Result<IPage<[Table2]>> findSearch(@PathVariable int currentIndex,
											  @PathVariable int pageSize,
											  @RequestBody  Map<String,Object> searchMap){
		IPage<[Table2]> list = [table2]Service.findSearch(currentIndex,pageSize,searchMap);
		return Result.success(list);
	}

	/**
	 * 增加
	 * @param [table2]
	 */
	@PostMapping(consumes = "application/json")
	public Result<Boolean> add(@RequestBody [Table2] [table2]) {
		return Result.success([table2]Service.save([table2]));
	}

	/**
	 * 修改
	 * @param [table2]
	 */
	@PutMapping
	public Result<Boolean> update(@RequestBody [Table2] [table2]){
		return Result.success([table2]Service.updateById([table2])) ;
	}

	/**
	 * 删除
	 * @param [key2]
	 */
	@DeleteMapping("{[key2]}")
	public Result<Boolean> deleteById(@PathVariable String [key2]){
		return Result.success([table2]Service.removeById([key2]));
	}
}
