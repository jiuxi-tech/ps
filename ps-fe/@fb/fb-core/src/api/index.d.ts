/**
 * api/index.js
 */

import axios, { AxiosInstance, AxiosResponse, AxiosError } from 'axios';

interface ResponseReader {
  codePath: string;
  successCode: string;
  messagePath: string;
  resultPath: string;
}

interface ResultFormat {
  codePath: string;
  messagePath: string;
  resultPath: string;
}

interface RequestLog {
  data: any[];
  importantApi: any;
  creatLog(url: string, data: any, status?: string): any;
  changeLogStatus(log: any, newStatus: string): void;
  getLogByURL(url: string, data: any): any[];
}

interface ApiError {
  [key: string]: Function[];
}

interface Api {
  instance: AxiosInstance;
  error: ApiError;
  responseReader: ResponseReader;
}

interface GetDataResult {
  code: any;
  message: any;
  result: any;
}

declare const requsetLog: RequestLog;

declare const instance: AxiosInstance;

declare const api: Api;

declare function getData(data: any, resultFormat: ResultFormat): GetDataResult;

declare function success(response: AxiosResponse): any;

declare function fail(error: AxiosError): any;

declare function param(url: string, data?: any, option?: any): any;

export default api;

export {
  requsetLog,
  instance,
  api,
  getData,
  success,
  fail,
  param,
  ResponseReader,
  ResultFormat,
  RequestLog,
  ApiError,
  Api,
  GetDataResult
};