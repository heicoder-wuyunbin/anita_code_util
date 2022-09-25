package [package].service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import [package].pojo.[Table2];
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author wuyunbin
 */
public interface [Table2]Service extends SuperService<[Table2]> {
    IPage<[Table2]> getList(int currentIndex,  int pageSize);
}