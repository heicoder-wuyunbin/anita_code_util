package [package].service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import [package].mapper.[Table2]Mapper;
import [package].pojo.[Table2];
import [package].service.[Table2]Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author wuyunbin
 */
@Service
public class [Table2]ServiceImpl extends SuperServiceImpl<[Table2]Mapper, [Table2]> implements [Table2]Service {

	@Autowired
	private [Table2]Mapper [table2]Mapper;
	@Override
	public IPage<[Table2]> getList(int currentIndex,  int pageSize) {
		LambdaQueryWrapper<[Table2]> query=new LambdaQueryWrapper<>();

		return this.page(new Page<>(currentIndex,pageSize), query);
	}
}