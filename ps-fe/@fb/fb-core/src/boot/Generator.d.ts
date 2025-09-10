/**
 * Generator
 * (c) 2020 lincong1987
 */

interface GeneratorConfig {
  service?: any;
  router?: any;
  store?: any;
}

export class Generator {
  constructor(generator: GeneratorConfig);
  
  generator: GeneratorConfig;
  
  init(): void;
}

export default Generator;