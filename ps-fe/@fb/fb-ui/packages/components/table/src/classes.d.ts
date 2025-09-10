/**
 * classes
 * (c) 2021 lincong1987
 */

export class ClassList {
  constructor(el: Element);
  
  el: Element;
  list: DOMTokenList;
  
  add(name: string): this;
  remove(name: string | RegExp): this;
  removeMatching(re: RegExp): this;
  toggle(name: string, force?: boolean): this;
  array(): string[];
  has(name: string): boolean;
  contains(name: string): boolean;
}

export default function classes(el: Element): ClassList;