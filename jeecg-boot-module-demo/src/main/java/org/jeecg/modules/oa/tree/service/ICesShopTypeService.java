package org.jeecg.modules.oa.tree.service;

import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.modules.oa.tree.entity.CesShopType;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * @Description: 商品分类
 * @Author: jeecg-boot
 * @Date:   2023-09-25
 * @Version: V1.0
 */
public interface ICesShopTypeService extends IService<CesShopType> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**
	 * 新增节点
	 *
	 * @param cesShopType
	 */
	void addCesShopType(CesShopType cesShopType);
	
	/**
   * 修改节点
   *
   * @param cesShopType
   * @throws JeecgBootException
   */
	void updateCesShopType(CesShopType cesShopType) throws JeecgBootException;
	
	/**
	 * 删除节点
	 *
	 * @param id
   * @throws JeecgBootException
	 */
	void deleteCesShopType(String id) throws JeecgBootException;

	  /**
	   * 查询所有数据，无分页
	   *
	   * @param queryWrapper
	   * @return List<CesShopType>
	   */
    List<CesShopType> queryTreeListNoPage(QueryWrapper<CesShopType> queryWrapper);

	/**
	 * 【vue3专用】根据父级编码加载分类字典的数据
	 *
	 * @param parentCode
	 * @return
	 */
	List<SelectTreeModel> queryListByCode(String parentCode);

	/**
	 * 【vue3专用】根据pid查询子节点集合
	 *
	 * @param pid
	 * @return
	 */
	List<SelectTreeModel> queryListByPid(String pid);

}
