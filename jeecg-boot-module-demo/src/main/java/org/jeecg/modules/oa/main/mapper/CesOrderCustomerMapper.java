package org.jeecg.modules.oa.main.mapper;

import java.util.List;
import org.jeecg.modules.oa.main.entity.CesOrderCustomer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 订单客户
 * @Author: jeecg-boot
 * @Date:   2023-09-25
 * @Version: V1.0
 */
public interface CesOrderCustomerMapper extends BaseMapper<CesOrderCustomer> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<CesOrderCustomer>
   */
	public List<CesOrderCustomer> selectByMainId(@Param("mainId") String mainId);
}
