package com.jiuxi.common.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CommonCommonListDiffUtil
 * @Description: 找出两个list中不同的元素工具
 * @Author 杨占锐
 * @Date  2022/1/18  16:59
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public class CommonListDiffUtil<T extends CommonListDiffUtil.ListItemPrimary> {

        private List<T> newList;

        private List<T> oldList;

        private List<List<T>> result = null;

        public CommonListDiffUtil(List<T> newList, List<T> oldList) {
            this.newList = newList;
            this.oldList = oldList;
        }

        /**
         * 获取需要新增的数据
         */
        public List<T> getInsert(){
            if(result == null){
                result = getDiff(newList, oldList);
            }
            return result.get(0);
        }

        /**
         * 获取需要修改的数据
         */
        public List<T> getUpdate(){
            if (result == null){
                result = getDiff(newList, oldList);
            }
            return result.get(1);
        }


        /**
         * 获取需要删除的数据
         */
        public List<T> getDelete(){
            if (result == null){
                result = getDiff(newList, oldList);
            }
            return result.get(2);
        }

        /**
         * 获取两个list中不同的数据
         *
         * @param newList 新数据
         * @param oldList 历史数据
         * @return List<List < ListItemPrimary>>  [0]:新增的， [1]:修改的， [2]:删除的
         * 注：数据中的主键不能为空
         */
        public List<List<T>> getDiff(List<T> newList, List<T> oldList) {
            List<List<T>> result = new ArrayList<>();
            List<T> ints = new ArrayList<>();
            List<T> upds = new ArrayList<>();
            List<T> dels = new ArrayList<>();
            result.add(ints);
            result.add(upds);
            result.add(dels);

            // 新数据为空
            if (CollectionUtil.isEmpty(newList)) {
                dels.addAll(oldList);
                return result;
            }

            // 旧数据为空
            if (CollectionUtil.isEmpty(oldList)) {
                ints.addAll(newList);
                return result;
            }

            // 保存旧数据
            Map<String, T> oldMap = new HashMap<>();
            for (T t : oldList) {
                String pKey = t.getPrimaryKey();
                if (StrUtil.isBlank(pKey)) {
                    throw new RuntimeException("------ primaryKey is null");
                }
                oldMap.put(pKey, t);
            }

            // 保存新数据
            Map<String, T> newMap = new HashMap<>();
            for (T t : newList) {
                String pKey = t.getPrimaryKey();
                if (StrUtil.isBlank(pKey)) {
                    throw new RuntimeException("------ primaryKey is null");
                }
                newMap.put(pKey, t);
            }

            // 找出新增和修改的数据
            for (T t : newList) {
                T nt = oldMap.get(t.getPrimaryKey());
                if (nt == null) {
                    ints.add(t);
                } else {
                    // 对象不相等时加入更新列表，可重写hashcode和equals方法实现对象相等，避免不必要的更新操作
                    if (!t.equals(nt)){
                        upds.add(t);
                    }
                }
            }

            // 找出删除的数据
            for (T t : oldList) {
                T nt = newMap.get(t.getPrimaryKey());
                if (nt == null) {
                    dels.add(t);
                }
            }

            return result;
        }

        public interface ListItemPrimary {
            String getPrimaryKey();
        }
    }

