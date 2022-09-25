package [package].service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import [package].mapper.SuperMapper;
import [package].service.SuperService;


/**
 * @author wuyunbin
 */

public class SuperServiceImpl<M extends SuperMapper<T>, T> extends ServiceImpl<M, T> implements SuperService<T> {
}