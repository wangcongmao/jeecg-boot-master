package org.jeecg.modules.oa.main.service;

import org.jeecg.modules.oa.main.entity.CesOrderCustomer;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 订单客户
 * @Author: jeecg-boot
 * @Date:   2023-09-25
 * @Version: V1.0
 */
public interface ICesOrderCustomerService extends IService<CesOrderCustomer> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<CesOrderCustomer>
	 */
	public List<CesOrderCustomer> selectByMainId(String mainId);
}
