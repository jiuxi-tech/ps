/**
 * EventObject
 * (c) 2021 lincong1987
 */

import EventBaseObject from './EventBaseObject';

export default class EventObject extends EventBaseObject {
  constructor(nativeEvent: Event);
  
  nativeEvent: Event;
  
  // Common properties
  altKey: boolean;
  bubbles: boolean;
  cancelable: boolean;
  ctrlKey: boolean;
  currentTarget: EventTarget;
  eventPhase: number;
  metaKey: boolean;
  shiftKey: boolean;
  target: EventTarget;
  timeStamp: number;
  view: Window;
  type: string;
  
  // Key event properties
  char?: string;
  charCode?: number;
  key?: string;
  keyCode?: number;
  which?: number;
  
  // Touch event properties
  touches?: TouchList;
  changedTouches?: TouchList;
  targetTouches?: TouchList;
  
  // Hash change properties
  newURL?: string;
  oldURL?: string;
  
  // Gesture change properties
  rotation?: number;
  scale?: number;
  
  // Mouse wheel properties
  deltaX?: number;
  deltaY?: number;
  delta?: number;
  buttons?: number;
  clientX?: number;
  clientY?: number;
  button?: number;
  offsetX?: number;
  relatedTarget?: EventTarget;
  fromElement?: Element;
  toElement?: Element;
  offsetY?: number;
  pageX?: number;
  pageY?: number;
  screenX?: number;
  screenY?: number;
  
  // Mouse event properties
  which: number;
}