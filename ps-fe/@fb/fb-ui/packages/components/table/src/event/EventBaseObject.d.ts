/**
 * EventBaseObject
 * (c) 2021 lincong1987
 */

export default class EventBaseObject {
  constructor();
  
  timeStamp: number;
  target: EventTarget | undefined;
  currentTarget: EventTarget | undefined;
  isEventObject: number;
  
  isDefaultPrevented(): boolean;
  isPropagationStopped(): boolean;
  isImmediatePropagationStopped(): boolean;
  
  preventDefault(): void;
  stopPropagation(): void;
  stopImmediatePropagation(): void;
  halt(immediate?: boolean): void;
}