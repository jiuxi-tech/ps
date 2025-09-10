/**
 * fontFamily 工具模块
 * 提供 font-family 相关的 Vue 组件属性和样式计算功能
 * @module fontFamily
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  fontFamily?: string
}

/**
 * fontFamily 组件属性定义
 */
export interface FontFamilyProps {
  /**
   * 设置 font-family 样式属性
   */
  family?: string
  
  /**
   * 设置 font-family 为 'Microsoft YaHei'
   */
  familyYahei?: boolean
  
  /**
   * 设置 font-family 为 'sans-serif'
   */
  familySans?: boolean
  
  /**
   * 设置 font-family 为 'SimSun'
   */
  familySong?: boolean
  
  /**
   * 设置 font-family 为 'serif'
   */
  familySerif?: boolean
  
  /**
   * 设置 font-family 为 'monospace'
   */
  familyMono?: boolean
  
  /**
   * 设置 font-family 为 'monospace'
   */
  familyCode?: boolean
  
  /**
   * 设置 font-family 为 'EmojiSymbols'
   */
  familyEmoji?: boolean
  
  /**
   * 设置 font-family 为 'sans-serif'
   */
  familyFallback?: boolean
  
  /**
   * 设置 font-family 为 'DingTalk-JinBuTi'
   */
  familyDingtalkJinbuti?: boolean
  
  /**
   * 设置 font-family 为 'DingTalk-JinBuTi'
   */
  familyJinbuti?: boolean
  
  /**
   * 设置 font-family 为 'FZZCHJW'
   */
  familyFzzchjw?: boolean
  
  /**
   * 设置 font-family 为 'Sansation_Light'
   */
  familySansationLight?: boolean
  
  /**
   * 设置 font-family 为 'YouSheBiaoTiHei'
   */
  familyYoushebiaotihei?: boolean
}

/**
 * fontFamily 样式计算函数
 * @returns 包含 fontFamily 样式属性的对象
 */
export declare function style(this: FontFamilyProps): Pick<CSSProperties, 'fontFamily'>

/**
 * fontFamily 模块默认导出
 */
interface FontFamilyModule {
  props: FontFamilyProps
  style: typeof style
}

export default FontFamilyModule