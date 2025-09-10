/**
 * keyCode - 键盘按键码工具集
 * @description 提供常用键盘按键码常量和检测函数
 */

/**
 * 键盘按键码工具集合
 */
export interface KeyCode {
  /**
   * MAC_ENTER 键 (3)
   */
  MAC_ENTER: number;

  /**
   * BACKSPACE 键 (8)
   */
  BACKSPACE: number;

  /**
   * TAB 键 (9)
   */
  TAB: number;

  /**
   * NUMLOCK 键在 FF/Safari Mac 上的值 (12)
   */
  NUM_CENTER: number;

  /**
   * ENTER 键 (13)
   */
  ENTER: number;

  /**
   * SHIFT 键 (16)
   */
  SHIFT: number;

  /**
   * CTRL 键 (17)
   */
  CTRL: number;

  /**
   * ALT 键 (18)
   */
  ALT: number;

  /**
   * PAUSE 键 (19)
   */
  PAUSE: number;

  /**
   * CAPS_LOCK 键 (20)
   */
  CAPS_LOCK: number;

  /**
   * ESC 键 (27)
   */
  ESC: number;

  /**
   * SPACE 键 (32)
   */
  SPACE: number;

  /**
   * PAGE_UP 键 (33)
   */
  PAGE_UP: number;

  /**
   * PAGE_DOWN 键 (34)
   */
  PAGE_DOWN: number;

  /**
   * END 键 (35)
   */
  END: number;

  /**
   * HOME 键 (36)
   */
  HOME: number;

  /**
   * LEFT 方向键 (37)
   */
  LEFT: number;

  /**
   * UP 方向键 (38)
   */
  UP: number;

  /**
   * RIGHT 方向键 (39)
   */
  RIGHT: number;

  /**
   * DOWN 方向键 (40)
   */
  DOWN: number;

  /**
   * PRINT_SCREEN 键 (44)
   */
  PRINT_SCREEN: number;

  /**
   * INSERT 键 (45)
   */
  INSERT: number;

  /**
   * DELETE 键 (46)
   */
  DELETE: number;

  /**
   * 数字键 0 (48)
   */
  ZERO: number;

  /**
   * 数字键 1 (49)
   */
  ONE: number;

  /**
   * 数字键 2 (50)
   */
  TWO: number;

  /**
   * 数字键 3 (51)
   */
  THREE: number;

  /**
   * 数字键 4 (52)
   */
  FOUR: number;

  /**
   * 数字键 5 (53)
   */
  FIVE: number;

  /**
   * 数字键 6 (54)
   */
  SIX: number;

  /**
   * 数字键 7 (55)
   */
  SEVEN: number;

  /**
   * 数字键 8 (56)
   */
  EIGHT: number;

  /**
   * 数字键 9 (57)
   */
  NINE: number;

  /**
   * 问号键 (63)
   */
  QUESTION_MARK: number;

  /**
   * 字母键 A (65)
   */
  A: number;

  /**
   * 字母键 B (66)
   */
  B: number;

  /**
   * 字母键 C (67)
   */
  C: number;

  /**
   * 字母键 D (68)
   */
  D: number;

  /**
   * 字母键 E (69)
   */
  E: number;

  /**
   * 字母键 F (70)
   */
  F: number;

  /**
   * 字母键 G (71)
   */
  G: number;

  /**
   * 字母键 H (72)
   */
  H: number;

  /**
   * 字母键 I (73)
   */
  I: number;

  /**
   * 字母键 J (74)
   */
  J: number;

  /**
   * 字母键 K (75)
   */
  K: number;

  /**
   * 字母键 L (76)
   */
  L: number;

  /**
   * 字母键 M (77)
   */
  M: number;

  /**
   * 字母键 N (78)
   */
  N: number;

  /**
   * 字母键 O (79)
   */
  O: number;

  /**
   * 字母键 P (80)
   */
  P: number;

  /**
   * 字母键 Q (81)
   */
  Q: number;

  /**
   * 字母键 R (82)
   */
  R: number;

  /**
   * 字母键 S (83)
   */
  S: number;

  /**
   * 字母键 T (84)
   */
  T: number;

  /**
   * 字母键 U (85)
   */
  U: number;

  /**
   * 字母键 V (86)
   */
  V: number;

  /**
   * 字母键 W (87)
   */
  W: number;

  /**
   * 字母键 X (88)
   */
  X: number;

  /**
   * 字母键 Y (89)
   */
  Y: number;

  /**
   * 字母键 Z (90)
   */
  Z: number;

  /**
   * META 键 (WIN_KEY_LEFT) (91)
   */
  META: number;

  /**
   * 右侧 WIN_KEY (92)
   */
  WIN_KEY_RIGHT: number;

  /**
   * CONTEXT_MENU 键 (93)
   */
  CONTEXT_MENU: number;

  /**
   * 数字键盘 0 (96)
   */
  NUM_ZERO: number;

  /**
   * 数字键盘 1 (97)
   */
  NUM_ONE: number;

  /**
   * 数字键盘 2 (98)
   */
  NUM_TWO: number;

  /**
   * 数字键盘 3 (99)
   */
  NUM_THREE: number;

  /**
   * 数字键盘 4 (100)
   */
  NUM_FOUR: number;

  /**
   * 数字键盘 5 (101)
   */
  NUM_FIVE: number;

  /**
   * 数字键盘 6 (102)
   */
  NUM_SIX: number;

  /**
   * 数字键盘 7 (103)
   */
  NUM_SEVEN: number;

  /**
   * 数字键盘 8 (104)
   */
  NUM_EIGHT: number;

  /**
   * 数字键盘 9 (105)
   */
  NUM_NINE: number;

  /**
   * 数字键盘 * (106)
   */
  NUM_MULTIPLY: number;

  /**
   * 数字键盘 + (107)
   */
  NUM_PLUS: number;

  /**
   * 数字键盘 - (109)
   */
  NUM_MINUS: number;

  /**
   * 数字键盘 . (110)
   */
  NUM_PERIOD: number;

  /**
   * 数字键盘 / (111)
   */
  NUM_DIVISION: number;

  /**
   * F1 功能键 (112)
   */
  F1: number;

  /**
   * F2 功能键 (113)
   */
  F2: number;

  /**
   * F3 功能键 (114)
   */
  F3: number;

  /**
   * F4 功能键 (115)
   */
  F4: number;

  /**
   * F5 功能键 (116)
   */
  F5: number;

  /**
   * F6 功能键 (117)
   */
  F6: number;

  /**
   * F7 功能键 (118)
   */
  F7: number;

  /**
   * F8 功能键 (119)
   */
  F8: number;

  /**
   * F9 功能键 (120)
   */
  F9: number;

  /**
   * F10 功能键 (121)
   */
  F10: number;

  /**
   * F11 功能键 (122)
   */
  F11: number;

  /**
   * F12 功能键 (123)
   */
  F12: number;

  /**
   * NUMLOCK 键 (144)
   */
  NUMLOCK: number;

  /**
   * 分号键 (186)
   */
  SEMICOLON: number;

  /**
   * 减号键 (189)
   */
  DASH: number;

  /**
   * 等号键 (187)
   */
  EQUALS: number;

  /**
   * 逗号键 (188)
   */
  COMMA: number;

  /**
   * 句号键 (190)
   */
  PERIOD: number;

  /**
   * 斜杠键 (191)
   */
  SLASH: number;

  /**
   * 撇号键 (192)
   */
  APOSTROPHE: number;

  /**
   * 单引号键 (222)
   */
  SINGLE_QUOTE: number;

  /**
   * 左方括号键 (219)
   */
  OPEN_SQUARE_BRACKET: number;

  /**
   * 反斜杠键 (220)
   */
  BACKSLASH: number;

  /**
   * 右方括号键 (221)
   */
  CLOSE_SQUARE_BRACKET: number;

  /**
   * WIN_KEY (224)
   */
  WIN_KEY: number;

  /**
   * MAC_FF_META (224)
   */
  MAC_FF_META: number;

  /**
   * WIN_IME (229)
   */
  WIN_IME: number;

  /**
   * 检测是否为文本修改按键事件
   * 
   * @param e - 键盘事件对象
   * @returns 如果是文本修改按键事件则返回 true
   * @example
   * // 检测是否为文本修改按键事件
   * element.addEventListener('keydown', function(e) {
   *   if (KeyCode.isTextModifyingKeyEvent(e)) {
   *     console.log('这是文本修改按键事件');
   *   }
   * });
   */
  isTextModifyingKeyEvent(e: KeyboardEvent): boolean;

  /**
   * 检测是否为字符键
   * 
   * @param keyCode - 键码
   * @returns 如果是字符键则返回 true
   * @example
   * // 检测是否为字符键
   * if (KeyCode.isCharacterKey(65)) {
   *   console.log('这是字符键 A');
   * }
   */
  isCharacterKey(keyCode: number): boolean;
}

/**
 * KeyCode 默认导出
 */
declare const keyCode: KeyCode;

export default keyCode;