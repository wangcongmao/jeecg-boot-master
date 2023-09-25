package org.jeecg.modules.oa.main.service;

import org.jeecg.modules.oa.main.entity.CesOrderGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 订单商品
 * @Author: jeecg-boot
 * @Date:   2023-09-25
 * @Version: V1.0
 */
public interface ICesOrderGoodsService extends IService<CesOrderGoods> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<CesOrderGoods>
	 */
	public List<CesOrderGoods> selectByMainId(String mainId);
}
