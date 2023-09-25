package org.jeecg.modules.oa.main.mapper;

import java.util.List;
import org.jeecg.modules.oa.main.entity.CesOrderGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 订单商品
 * @Author: jeecg-boot
 * @Date:   2023-09-25
 * @Version: V1.0
 */
public interface CesOrderGoodsMapper extends BaseMapper<CesOrderGoods> {

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
   * @return List<CesOrderGoods>
   */
	public List<CesOrderGoods> selectByMainId(@Param("mainId") String mainId);
}
