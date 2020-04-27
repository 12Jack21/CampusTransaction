package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.dao.CommodityDAO;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Estimate;
import com.example.transaction.pojo.Type;
import com.example.transaction.service.CommodityService;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Timestamp;
import java.util.*;

/**
 * @Author: 高战立
 * @Date: 2020/4/26 15:46
 * @Content:
 */

@Service
public class CommodityServiceImpl implements CommodityService {
    //商品名搜索； 搜索之后排序方式：崭新程度、价格区间、信誉排序  模糊查找
    //商品分类、修改商品信息
    //插入、删除、展示所有商品
    private final CommodityDAO commodityDAO;
    private int count = 0;
    @Autowired
    CommodityServiceImpl(CommodityDAO commodityDAO){
        this.commodityDAO = commodityDAO;
    }

    /**
     * 商品名称模糊检索，崭新程度排序
     * @param name 商品名
     * count为偶数:顺序（从小到大）；奇数:倒序
     * @return Commodity数组
     */
    public List<Commodity> getByNameSortedByNewness(String name){
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "%"+name+"%");
        if(count%2 == 0)
            queryWrapper.orderByAsc("newness");
        else
            queryWrapper.orderByDesc("newness");
        count++;
        List<Commodity> commodities = commodityDAO.selectWithCondition(queryWrapper); //查询得到的所有结果，但是需要考虑商品的数据和截止日期
        return selectByCountAndDate(commodities);
    }

    /**
     * 根据类型分类
     * @param typeId 标签
     * @return Commodity数组
     */
    public List<Commodity> getByTypeId(Integer typeId){
        List<Commodity> commodities = selectByCountAndDate(commodityDAO.selectAllInfo()); //查询得到的所有结果，但是需要考虑商品的数据和截止日期
        List<Commodity> results = new ArrayList<>();
        for(Commodity commodity:commodities){
            for(Type type:commodity.getTypes()){
                if(type.getId().equals(typeId)){
                    results.add(commodity);
                    break;
                }
            }
        }
        return results;
    }

    /**
     * 根据价格区间筛选物品
     * @param low 最低价
     * @param high 最高价
     * @return Commodity数组
     */
    public List<Commodity> getBetweenPrice(String name, Integer low, Integer high){
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "%"+name+"%");
        queryWrapper.between("expected_price", low, high);
        List<Commodity> commodities = commodityDAO.selectWithCondition(queryWrapper);
        return selectByCountAndDate(commodities);
    }

    /**
     * 根据所有者信誉排序
     * @param name 商品名
     * @return Commodity数组
     */
    public List<Commodity> sortByCredit(String name){
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "%"+name+"%");
        List<Commodity> commodities = selectByCountAndDate(commodityDAO.selectWithCondition(queryWrapper));
        commodities.sort(new Comparator<Commodity>() {
            @Override
            public int compare(Commodity o1, Commodity o2) {
                Estimate estimate1 = o1.getCommodityList().getNotice().getUser().getEstimate();
                Estimate estimate2 = o2.getCommodityList().getNotice().getUser().getEstimate();
                double credit1 = estimate1.getSellCredit() + estimate1.getPurchaseCredit();
                double credit2 = estimate2.getSellCredit() + estimate2.getPurchaseCredit();

                if(credit1 > credit2)
                    return -1;
                else if(credit1 <credit2)
                    return 1;
                return 0;
            }
        });
        return commodities;
    }

    /**
     * 插入商品
     * @param commodity 商品
     * @return 执行结果
     */
    public responseFromServer insertCommodity(Commodity commodity){
        commodityDAO.insert(commodity);
        return responseFromServer.success();
    }

    /**
     * 更新商品信息
     * @param commodity 商品
     * @return 执行结果
     */
    public responseFromServer updateCommodity(Commodity commodity){
        if(commodityDAO.updateById(commodity) != 1){
            /*回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        else{
            return responseFromServer.success();
        }
    }

    /**
     * 筛选有剩余且未截止的商品
     * @param commodities 商品集合
     * @return 商品集合
     */
    public List<Commodity> selectByCountAndDate(List<Commodity> commodities){
        List<Commodity> results = new ArrayList<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //当前时间
        for(Commodity commodity : commodities){
            if(commodity.getCommodityList().getCount() > 0 && timestamp.before(commodity.getCommodityList().getNotice().getEndTime()))
                results.add(commodity);
        }
        return results;
    }
}
