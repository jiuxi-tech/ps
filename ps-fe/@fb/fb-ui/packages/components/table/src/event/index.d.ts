/**
 * index
 * (c) 2021 lincong1987
 */

export interface EventListener {
  remove(): void;
  cancel(): void;
}

export default function addEventListener(
  target: EventTarget,
  eventType: string,
  callback: (e: Event) => void,
  option?: boolean | AddEventListenerOptions
): EventListener;