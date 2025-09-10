import ApiLoader from "./api-loader";

let ApiLoaderInstance = new ApiLoader();

export const initApiLoader = config => {
  ApiLoaderInstance = new ApiLoader(config);
  ApiLoaderInstance.load();
};

export { ApiLoaderInstance };
