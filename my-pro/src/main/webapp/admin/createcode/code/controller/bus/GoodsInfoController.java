package com.bdh.controller.bus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.bdh.controller.base.BaseController;
import com.bdh.entity.Page;
import com.bdh.util.DateUtil;
import com.bdh.util.ObjectExcelView;
import com.bdh.util.Tools;
import com.bdh.entity.PageData;
import com.bdh.service.bus.GoodsInfoService;

/** 
 * 说明：商品信息
 * 作者：shaojie
 * 时间：2020-10-25
 * 官网：www.qdkjchina.com
 */
@Controller
@RequestMapping("/goodsinfo")
public class GoodsInfoController extends BaseController {
	
	@Autowired
	private GoodsInfoService goodsinfoService;
	
	/**新增
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("goodsinfo:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("GOODSINFO_ID", this.get32UUID());	//主键
		goodsinfoService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("goodsinfo:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		goodsinfoService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("goodsinfo:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		goodsinfoService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("goodsinfo:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = goodsinfoService.list(page);	//列出GoodsInfo列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@RequiresPermissions("goodsinfo:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = goodsinfoService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("goodsinfo:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			goodsinfoService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("商品名称");	//1
		titles.add("商品类型");	//2
		titles.add("商品金额");	//3
		titles.add("活动金额");	//4
		titles.add("内容");	//5
		titles.add("商品图片");	//6
		titles.add("售后服务");	//7
		titles.add("服务保障");	//8
		titles.add("服务热线");	//9
		titles.add("订购须知");	//10
		titles.add("商品详情");	//11
		titles.add("是否上架1，是2，否");	//12
		titles.add("商品简介");	//13
		titles.add("已售数量");	//14
		titles.add("是否首页展示");	//15
		titles.add("服务声明");	//16
		titles.add("库存数量");	//17
		titles.add("是否删除");	//18
		titles.add("操作人");	//19
		titles.add("操作日期");	//20
		dataMap.put("titles", titles);
		List<PageData> varOList = goodsinfoService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("GOODSNAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("GOODSTYPE"));	    //2
			vpd.put("var3", varOList.get(i).getString("GOODSAMOUNT"));	    //3
			vpd.put("var4", varOList.get(i).getString("AMOUNTACTIVITY"));	    //4
			vpd.put("var5", varOList.get(i).getString("GOODSCONTENT"));	    //5
			vpd.put("var6", varOList.get(i).getString("GOODSIMG"));	    //6
			vpd.put("var7", varOList.get(i).getString("AFTERSERVICE"));	    //7
			vpd.put("var8", varOList.get(i).getString("SERVICEGUARANTEE"));	    //8
			vpd.put("var9", varOList.get(i).getString("HOTLINE"));	    //9
			vpd.put("var10", varOList.get(i).getString("ORDERINGINFORMATION"));	    //10
			vpd.put("var11", varOList.get(i).getString("GOODSDETAILS"));	    //11
			vpd.put("var12", varOList.get(i).getString("ISUPPERSHELF"));	    //12
			vpd.put("var13", varOList.get(i).getString("GOODSSYNOPSIS"));	    //13
			vpd.put("var14", varOList.get(i).getString("QUANTITYSOLD"));	    //14
			vpd.put("var15", varOList.get(i).getString("ISSHOW"));	    //15
			vpd.put("var16", varOList.get(i).getString("SERVICESTATEMENT"));	    //16
			vpd.put("var17", varOList.get(i).getString("INVENTORYQUANTITY"));	    //17
			vpd.put("var18", varOList.get(i).getString("ISDELETE"));	    //18
			vpd.put("var19", varOList.get(i).getString("OPUSER"));	    //19
			vpd.put("var20", varOList.get(i).getString("OPDATE"));	    //20
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
