package [package].service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import [package].pojo.[Table2];
import java.util.Map;
/**
 * @author [author]
 */
public interface [Table2]Service extends SuperService<[Table2]> {
    IPage<[Table2]> getList(int currentIndex,  int pageSize);
    IPage<[Table2]> findSearch(int currentIndex,int pageSize,Map<String, Object> searchMap);
}