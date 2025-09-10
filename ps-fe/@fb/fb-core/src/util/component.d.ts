/**
 * component
 * (c) 2020 lincong1987
 */

import Vue from 'vue';

/**
 * 向上查找组件
 * @param context 
 * @param componentName 
 */
export function closest(context: Vue, componentName: string | string[]): Vue | null;

/**
 * 向下查找组件
 * @param context 
 * @param componentName 
 */
export function find(context: Vue, componentName: string | string[]): Vue | null;