package org.jeecg.modules.oa.tree.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.modules.oa.tree.entity.CesShopType;
import org.jeecg.modules.oa.tree.service.ICesShopTypeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 商品分类
 * @Author: jeecg-boot
 * @Date:   2023-09-25
 * @Version: V1.0
 */
@Api(tags="商品分类")
@RestController
@RequestMapping("/tree/cesShopType")
@Slf4j
public class CesShopTypeController extends JeecgController<CesShopType, ICesShopTypeService>{
	@Autowired
	private ICesShopTypeService cesShopTypeService;

	/**
	 * 分页列表查询
	 *
	 * @param cesShopType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "商品分类-分页列表查询")
	@ApiOperation(value="商品分类-分页列表查询", notes="商品分类-分页列表查询")
	@GetMapping(value = "/rootList")
	public Result<IPage<CesShopType>> queryPageList(CesShopType cesShopType,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String hasQuery = req.getParameter("hasQuery");
        if(hasQuery != null && "true".equals(hasQuery)){
            QueryWrapper<CesShopType> queryWrapper =  QueryGenerator.initQueryWrapper(cesShopType, req.getParameterMap());
            List<CesShopType> list = cesShopTypeService.queryTreeListNoPage(queryWrapper);
            IPage<CesShopType> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        }else{
            String parentId = cesShopType.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            cesShopType.setPid(null);
            QueryWrapper<CesShopType> queryWrapper = QueryGenerator.initQueryWrapper(cesShopType, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId);
            Page<CesShopType> page = new Page<CesShopType>(pageNo, pageSize);
            IPage<CesShopType> pageList = cesShopTypeService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
	}

	 /**
	  * 【vue3专用】加载节点的子数据
	  *
	  * @param pid
	  * @return
	  */
	 @RequestMapping(value = "/loadTreeChildren", method = RequestMethod.GET)
	 public Result<List<SelectTreeModel>> loadTreeChildren(@RequestParam(name = "pid") String pid) {
		 Result<List<SelectTreeModel>> result = new Result<>();
		 try {
			 List<SelectTreeModel> ls = cesShopTypeService.queryListByPid(pid);
			 result.setResult(ls);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 e.printStackTrace();
			 result.setMessage(e.getMessage());
			 result.setSuccess(false);
		 }
		 return result;
	 }

	 /**
	  * 【vue3专用】加载一级节点/如果是同步 则所有数据
	  *
	  * @param async
	  * @param pcode
	  * @return
	  */
	 @RequestMapping(value = "/loadTreeRoot", method = RequestMethod.GET)
	 public Result<List<SelectTreeModel>> loadTreeRoot(@RequestParam(name = "async") Boolean async, @RequestParam(name = "pcode") String pcode) {
		 Result<List<SelectTreeModel>> result = new Result<>();
		 try {
			 List<SelectTreeModel> ls = cesShopTypeService.queryListByCode(pcode);
			 if (!async) {
				 loadAllChildren(ls);
			 }
			 result.setResult(ls);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 e.printStackTrace();
			 result.setMessage(e.getMessage());
			 result.setSuccess(false);
		 }
		 return result;
	 }

	 /**
	  * 【vue3专用】递归求子节点 同步加载用到
	  *
	  * @param ls
	  */
	 private void loadAllChildren(List<SelectTreeModel> ls) {
		 for (SelectTreeModel tsm : ls) {
			 List<SelectTreeModel> temp = cesShopTypeService.queryListByPid(tsm.getKey());
			 if (temp != null && temp.size() > 0) {
				 tsm.setChildren(temp);
				 loadAllChildren(temp);
			 }
		 }
	 }

	 /**
      * 获取子数据
      * @param cesShopType
      * @param req
      * @return
      */
	//@AutoLog(value = "商品分类-获取子数据")
	@ApiOperation(value="商品分类-获取子数据", notes="商品分类-获取子数据")
	@GetMapping(value = "/childList")
	public Result<IPage<CesShopType>> queryPageList(CesShopType cesShopType,HttpServletRequest req) {
		QueryWrapper<CesShopType> queryWrapper = QueryGenerator.initQueryWrapper(cesShopType, req.getParameterMap());
		List<CesShopType> list = cesShopTypeService.list(queryWrapper);
		IPage<CesShopType> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
		return Result.OK(pageList);
	}

    /**
      * 批量查询子节点
      * @param parentIds 父ID（多个采用半角逗号分割）
      * @return 返回 IPage
      * @param parentIds
      * @return
      */
	//@AutoLog(value = "商品分类-批量获取子数据")
    @ApiOperation(value="商品分类-批量获取子数据", notes="商品分类-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<CesShopType> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<CesShopType> list = cesShopTypeService.list(queryWrapper);
            IPage<CesShopType> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }
	
	/**
	 *   添加
	 *
	 * @param cesShopType
	 * @return
	 */
	@AutoLog(value = "商品分类-添加")
	@ApiOperation(value="商品分类-添加", notes="商品分类-添加")
    @RequiresPermissions("tree:ces_shop_type:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CesShopType cesShopType) {
		cesShopTypeService.addCesShopType(cesShopType);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param cesShopType
	 * @return
	 */
	@AutoLog(value = "商品分类-编辑")
	@ApiOperation(value="商品分类-编辑", notes="商品分类-编辑")
    @RequiresPermissions("tree:ces_shop_type:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CesShopType cesShopType) {
		cesShopTypeService.updateCesShopType(cesShopType);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "商品分类-通过id删除")
	@ApiOperation(value="商品分类-通过id删除", notes="商品分类-通过id删除")
    @RequiresPermissions("tree:ces_shop_type:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		cesShopTypeService.deleteCesShopType(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "商品分类-批量删除")
	@ApiOperation(value="商品分类-批量删除", notes="商品分类-批量删除")
    @RequiresPermissions("tree:ces_shop_type:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cesShopTypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "商品分类-通过id查询")
	@ApiOperation(value="商品分类-通过id查询", notes="商品分类-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<CesShopType> queryById(@RequestParam(name="id",required=true) String id) {
		CesShopType cesShopType = cesShopTypeService.getById(id);
		if(cesShopType==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(cesShopType);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cesShopType
    */
    @RequiresPermissions("tree:ces_shop_type:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CesShopType cesShopType) {
		return super.exportXls(request, cesShopType, CesShopType.class, "商品分类");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("tree:ces_shop_type:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		return super.importExcel(request, response, CesShopType.class);
    }

}
