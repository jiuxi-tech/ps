/**
 * @fileOverview Kickass library to create and place poppers near their reference elements.
 * @version {{version}}
 * @license
 * Copyright (c) 2016 Federico Zivolo and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * keyCode - 键盘按键码工具集
 * @description 提供常用键盘按键码常量和检测函数
 */

/**
 * @namespace KeyCode
 * @desc 键盘按键码工具集合
 */

const KeyCode = {

    /**
     * @member {Number} MAC_ENTER
     * @desc MAC_ENTER 键 (3)
     */
    MAC_ENTER: 3,

    /**
     * @member {Number} BACKSPACE
     * @desc BACKSPACE 键 (8)
     */
    BACKSPACE: 8,

    /**
     * @member {Number} TAB
     * @desc TAB 键 (9)
     */
    TAB: 9,

    /**
     * @member {Number} NUM_CENTER
     * @desc NUMLOCK 键在 FF/Safari Mac 上的值 (12)
     */
    NUM_CENTER: 12,

    /**
     * @member {Number} ENTER
     * @desc ENTER 键 (13)
     */
    ENTER: 13,

    /**
     * @member {Number} SHIFT
     * @desc SHIFT 键 (16)
     */
    SHIFT: 16,

    /**
     * @member {Number} CTRL
     * @desc CTRL 键 (17)
     */
    CTRL: 17,

    /**
     * @member {Number} ALT
     * @desc ALT 键 (18)
     */
    ALT: 18,

    /**
     * @member {Number} PAUSE
     * @desc PAUSE 键 (19)
     */
    PAUSE: 19,

    /**
     * @member {Number} CAPS_LOCK
     * @desc CAPS_LOCK 键 (20)
     */
    CAPS_LOCK: 20,

    /**
     * @member {Number} ESC
     * @desc ESC 键 (27)
     */
    ESC: 27,

    /**
     * @member {Number} SPACE
     * @desc SPACE 键 (32)
     */
    SPACE: 32,

    /**
     * @member {Number} PAGE_UP
     * @desc PAGE_UP 键 (33)
     */
    PAGE_UP: 33,

    /**
     * @member {Number} PAGE_DOWN
     * @desc PAGE_DOWN 键 (34)
     */
    PAGE_DOWN: 34,

    /**
     * @member {Number} END
     * @desc END 键 (35)
     */
    END: 35,

    /**
     * @member {Number} HOME
     * @desc HOME 键 (36)
     */
    HOME: 36,

    /**
     * @member {Number} LEFT
     * @desc LEFT 方向键 (37)
     */
    LEFT: 37,

    /**
     * @member {Number} UP
     * @desc UP 方向键 (38)
     */
    UP: 38,

    /**
     * @member {Number} RIGHT
     * @desc RIGHT 方向键 (39)
     */
    RIGHT: 39,

    /**
     * @member {Number} DOWN
     * @desc DOWN 方向键 (40)
     */
    DOWN: 40,

    /**
     * @member {Number} PRINT_SCREEN
     * @desc PRINT_SCREEN 键 (44)
     */
    PRINT_SCREEN: 44,

    /**
     * @member {Number} INSERT
     * @desc INSERT 键 (45)
     */
    INSERT: 45,

    /**
     * @member {Number} DELETE
     * @desc DELETE 键 (46)
     */
    DELETE: 46,

    /**
     * @member {Number} ZERO
     * @desc 数字键 0 (48)
     */
    ZERO: 48,

    /**
     * @member {Number} ONE
     * @desc 数字键 1 (49)
     */
    ONE: 49,

    /**
     * @member {Number} TWO
     * @desc 数字键 2 (50)
     */
    TWO: 50,

    /**
     * @member {Number} THREE
     * @desc 数字键 3 (51)
     */
    THREE: 51,

    /**
     * @member {Number} FOUR
     * @desc 数字键 4 (52)
     */
    FOUR: 52,

    /**
     * @member {Number} FIVE
     * @desc 数字键 5 (53)
     */
    FIVE: 53,

    /**
     * @member {Number} SIX
     * @desc 数字键 6 (54)
     */
    SIX: 54,

    /**
     * @member {Number} SEVEN
     * @desc 数字键 7 (55)
     */
    SEVEN: 55,

    /**
     * @member {Number} EIGHT
     * @desc 数字键 8 (56)
     */
    EIGHT: 56,

    /**
     * @member {Number} NINE
     * @desc 数字键 9 (57)
     */
    NINE: 57,

    /**
     * @member {Number} QUESTION_MARK
     * @desc 问号键 (63)
     */
    QUESTION_MARK: 63,

    /**
     * @member {Number} A
     * @desc 字母键 A (65)
     */
    A: 65,

    /**
     * @member {Number} B
     * @desc 字母键 B (66)
     */
    B: 66,

    /**
     * @member {Number} C
     * @desc 字母键 C (67)
     */
    C: 67,

    /**
     * @member {Number} D
     * @desc 字母键 D (68)
     */
    D: 68,

    /**
     * @member {Number} E
     * @desc 字母键 E (69)
     */
    E: 69,

    /**
     * @member {Number} F
     * @desc 字母键 F (70)
     */
    F: 70,

    /**
     * @member {Number} G
     * @desc 字母键 G (71)
     */
    G: 71,

    /**
     * @member {Number} H
     * @desc 字母键 H (72)
     */
    H: 72,

    /**
     * @member {Number} I
     * @desc 字母键 I (73)
     */
    I: 73,

    /**
     * @member {Number} J
     * @desc 字母键 J (74)
     */
    J: 74,

    /**
     * @member {Number} K
     * @desc 字母键 K (75)
     */
    K: 75,

    /**
     * @member {Number} L
     * @desc 字母键 L (76)
     */
    L: 76,

    /**
     * @member {Number} M
     * @desc 字母键 M (77)
     */
    M: 77,

    /**
     * @member {Number} N
     * @desc 字母键 N (78)
     */
    N: 78,

    /**
     * @member {Number} O
     * @desc 字母键 O (79)
     */
    O: 79,

    /**
     * @member {Number} P
     * @desc 字母键 P (80)
     */
    P: 80,

    /**
     * @member {Number} Q
     * @desc 字母键 Q (81)
     */
    Q: 81,

    /**
     * @member {Number} R
     * @desc 字母键 R (82)
     */
    R: 82,

    /**
     * @member {Number} S
     * @desc 字母键 S (83)
     */
    S: 83,

    /**
     * @member {Number} T
     * @desc 字母键 T (84)
     */
    T: 84,

    /**
     * @member {Number} U
     * @desc 字母键 U (85)
     */
    U: 85,

    /**
     * @member {Number} V
     * @desc 字母键 V (86)
     */
    V: 86,

    /**
     * @member {Number} W
     * @desc 字母键 W (87)
     */
    W: 87,

    /**
     * @member {Number} X
     * @desc 字母键 X (88)
     */
    X: 88,

    /**
     * @member {Number} Y
     * @desc 字母键 Y (89)
     */
    Y: 89,

    /**
     * @member {Number} Z
     * @desc 字母键 Z (90)
     */
    Z: 90,

    /**
     * @member {Number} META
     * @desc META 键 (WIN_KEY_LEFT) (91)
     */
    META: 91,

    /**
     * @member {Number} WIN_KEY_RIGHT
     * @desc 右侧 WIN_KEY (92)
     */
    WIN_KEY_RIGHT: 92,

    /**
     * @member {Number} CONTEXT_MENU
     * @desc CONTEXT_MENU 键 (93)
     */
    CONTEXT_MENU: 93,

    /**
     * @member {Number} NUM_ZERO
     * @desc 数字键盘 0 (96)
     */
    NUM_ZERO: 96,

    /**
     * @member {Number} NUM_ONE
     * @desc 数字键盘 1 (97)
     */
    NUM_ONE: 97,

    /**
     * @member {Number} NUM_TWO
     * @desc 数字键盘 2 (98)
     */
    NUM_TWO: 98,

    /**
     * @member {Number} NUM_THREE
     * @desc 数字键盘 3 (99)
     */
    NUM_THREE: 99,

    /**
     * @member {Number} NUM_FOUR
     * @desc 数字键盘 4 (100)
     */
    NUM_FOUR: 100,

    /**
     * @member {Number} NUM_FIVE
     * @desc 数字键盘 5 (101)
     */
    NUM_FIVE: 101,

    /**
     * @member {Number} NUM_SIX
     * @desc 数字键盘 6 (102)
     */
    NUM_SIX: 102,

    /**
     * @member {Number} NUM_SEVEN
     * @desc 数字键盘 7 (103)
     */
    NUM_SEVEN: 103,

    /**
     * @member {Number} NUM_EIGHT
     * @desc 数字键盘 8 (104)
     */
    NUM_EIGHT: 104,

    /**
     * @member {Number} NUM_NINE
     * @desc 数字键盘 9 (105)
     */
    NUM_NINE: 105,

    /**
     * @member {Number} NUM_MULTIPLY
     * @desc 数字键盘 * (106)
     */
    NUM_MULTIPLY: 106,

    /**
     * @member {Number} NUM_PLUS
     * @desc 数字键盘 + (107)
     */
    NUM_PLUS: 107,

    /**
     * @member {Number} NUM_MINUS
     * @desc 数字键盘 - (109)
     */
    NUM_MINUS: 109,

    /**
     * @member {Number} NUM_PERIOD
     * @desc 数字键盘 . (110)
     */
    NUM_PERIOD: 110,

    /**
     * @member {Number} NUM_DIVISION
     * @desc 数字键盘 / (111)
     */
    NUM_DIVISION: 111,

    /**
     * @member {Number} F1
     * @desc F1 功能键 (112)
     */
    F1: 112,

    /**
     * @member {Number} F2
     * @desc F2 功能键 (113)
     */
    F2: 113,

    /**
     * @member {Number} F3
     * @desc F3 功能键 (114)
     */
    F3: 114,

    /**
     * @member {Number} F4
     * @desc F4 功能键 (115)
     */
    F4: 115,

    /**
     * @member {Number} F5
     * @desc F5 功能键 (116)
     */
    F5: 116,

    /**
     * @member {Number} F6
     * @desc F6 功能键 (117)
     */
    F6: 117,

    /**
     * @member {Number} F7
     * @desc F7 功能键 (118)
     */
    F7: 118,

    /**
     * @member {Number} F8
     * @desc F8 功能键 (119)
     */
    F8: 119,

    /**
     * @member {Number} F9
     * @desc F9 功能键 (120)
     */
    F9: 120,

    /**
     * @member {Number} F10
     * @desc F10 功能键 (121)
     */
    F10: 121,

    /**
     * @member {Number} F11
     * @desc F11 功能键 (122)
     */
    F11: 122,

    /**
     * @member {Number} F12
     * @desc F12 功能键 (123)
     */
    F12: 123,

    /**
     * @member {Number} NUMLOCK
     * @desc NUMLOCK 键 (144)
     */
    NUMLOCK: 144,

    /**
     * @member {Number} SEMICOLON
     * @desc 分号键 (186)
     */
    SEMICOLON: 186,

    /**
     * @member {Number} DASH
     * @desc 减号键 (189)
     */
    DASH: 189,

    /**
     * @member {Number} EQUALS
     * @desc 等号键 (187)
     */
    EQUALS: 187,

    /**
     * @member {Number} COMMA
     * @desc 逗号键 (188)
     */
    COMMA: 188,

    /**
     * @member {Number} PERIOD
     * @desc 句号键 (190)
     */
    PERIOD: 190,

    /**
     * @member {Number} SLASH
     * @desc 斜杠键 (191)
     */
    SLASH: 191,

    /**
     * @member {Number} APOSTROPHE
     * @desc 撇号键 (192)
     */
    APOSTROPHE: 192,

    /**
     * @member {Number} SINGLE_QUOTE
     * @desc 单引号键 (222)
     */
    SINGLE_QUOTE: 222,

    /**
     * @member {Number} OPEN_SQUARE_BRACKET
     * @desc 左方括号键 (219)
     */
    OPEN_SQUARE_BRACKET: 219,

    /**
     * @member {Number} BACKSLASH
     * @desc 反斜杠键 (220)
     */
    BACKSLASH: 220,

    /**
     * @member {Number} CLOSE_SQUARE_BRACKET
     * @desc 右方括号键 (221)
     */
    CLOSE_SQUARE_BRACKET: 221,

    /**
     * @member {Number} WIN_KEY
     * @desc WIN_KEY (224)
     */
    WIN_KEY: 224,

    /**
     * @member {Number} MAC_FF_META
     * @desc MAC_FF_META (224)
     */
    MAC_FF_META: 224,

    /**
     * @member {Number} WIN_IME
     * @desc WIN_IME (229)
     */
    WIN_IME: 229
};

/**
 * @desc 检测是否为文本修改按键事件
 * @param {KeyboardEvent} e - 键盘事件对象
 * @returns {Boolean} 如果是文本修改按键事件则返回 true
 * @example
 * // 检测是否为文本修改按键事件
 * element.addEventListener('keydown', function(e) {
 *   if (KeyCode.isTextModifyingKeyEvent(e)) {
 *     console.log('这是文本修改按键事件');
 *   }
 * });
 */
KeyCode.isTextModifyingKeyEvent = function isTextModifyingKeyEvent(e) {
    const keyCode = e.keyCode;
    if (
        (e.altKey && !e.ctrlKey)
      || e.metaKey
      // Function keys don't generate text
      || (keyCode >= KeyCode.F1 && keyCode <= KeyCode.F12)
    ) {
        return false;
    }

    // The following keys are quite harmless, even in combination with
    // CTRL, ALT or SHIFT.
    switch (keyCode) {
        case KeyCode.ALT:
        case KeyCode.CAPS_LOCK:
        case KeyCode.CONTEXT_MENU:
        case KeyCode.CTRL:
        case KeyCode.DOWN:
        case KeyCode.END:
        case KeyCode.ESC:
        case KeyCode.HOME:
        case KeyCode.INSERT:
        case KeyCode.LEFT:
        case KeyCode.MAC_FF_META:
        case KeyCode.META:
        case KeyCode.NUMLOCK:
        case KeyCode.NUM_CENTER:
        case KeyCode.PAGE_DOWN:
        case KeyCode.PAGE_UP:
        case KeyCode.PAUSE:
        case KeyCode.PRINT_SCREEN:
        case KeyCode.RIGHT:
        case KeyCode.SHIFT:
        case KeyCode.UP:
        case KeyCode.WIN_KEY:
        case KeyCode.WIN_KEY_RIGHT:
            return false;
        default:
            return true;
    }
};

/**
 * @desc 检测是否为字符键
 * @param {Number} keyCode - 键码
 * @returns {Boolean} 如果是字符键则返回 true
 * @example
 * // 检测是否为字符键
 * if (KeyCode.isCharacterKey(65)) {
 *   console.log('这是字符键 A');
 * }
 */
KeyCode.isCharacterKey = function isCharacterKey(keyCode) {
    if (keyCode >= KeyCode.ZERO && keyCode <= KeyCode.NINE) {
        return true;
    }

    if (keyCode >= KeyCode.NUM_ZERO && keyCode <= KeyCode.NUM_MULTIPLY) {
        return true;
    }

    if (keyCode >= KeyCode.A && keyCode <= KeyCode.Z) {
        return true;
    }

    // Safari sends zero key code for non-latin characters.
    if (window.navigation.userAgent.indexOf('WebKit') !== -1 && keyCode === 0) {
        return true;
    }

    switch (keyCode) {
        case KeyCode.SPACE:
        case KeyCode.QUESTION_MARK:
        case KeyCode.NUM_PLUS:
        case KeyCode.NUM_MINUS:
        case KeyCode.NUM_PERIOD:
        case KeyCode.NUM_DIVISION:
        case KeyCode.SEMICOLON:
        case KeyCode.DASH:
        case KeyCode.EQUALS:
        case KeyCode.COMMA:
        case KeyCode.PERIOD:
        case KeyCode.SLASH:
        case KeyCode.APOSTROPHE:
        case KeyCode.SINGLE_QUOTE:
        case KeyCode.OPEN_SQUARE_BRACKET:
        case KeyCode.BACKSLASH:
        case KeyCode.CLOSE_SQUARE_BRACKET:
            return true;
        default:
            return false;
    }
};

export default KeyCode;