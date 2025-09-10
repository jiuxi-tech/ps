/**
 * App
 * (c) 2021 lincong1987
 */

interface PageData {
  app: string;
  dialog: any;
  myService: any;
  myReset: any;
  [key: string]: any;
}

interface Buttons {
  save?: {
    disabled: boolean;
    show: boolean;
  };
  edit?: {
    disabled: boolean;
    show: boolean;
  };
  del?: {
    disabled: boolean;
    show: boolean;
  };
}

interface Table {
  primaryKey: string;
  columns: any[];
  data: any[];
}

interface Reader {
  table: string;
}

interface SearchPageData extends PageData {
  buttons: Buttons;
  table: Table;
  reader: Reader;
}

interface SavePageData extends PageData {
  // SavePage specific data
}

interface EditPageData extends PageData {
  // EditPage specific data
}

interface ViewPageData extends PageData {
  // ViewPage specific data
}

interface PageMethods {
  doFill(): void;
  handleClose(): void;
  handleReset(): void;
}

interface SearchPageMethods extends PageMethods {
  handleQuery(): void;
  handleTableSelect(row: any, selectedRows: any[]): void;
  handleSave(): void;
  handleEdit(): void;
  handleView(row?: any): void;
  handleDel(): void;
}

interface SavePageMethods extends PageMethods {
  handleSave(): void;
}

interface EditPageMethods extends PageMethods {
  handleSave(): void;
}

interface ViewPageMethods extends PageMethods {
  // ViewPage specific methods
}

interface PageComponent {
  directives: {
    autoheight: any;
    permission: any;
  };
  props: {
    service: {
      type: ObjectConstructor;
      required: boolean;
    };
    data: {
      type: ObjectConstructor;
      required: boolean;
    };
  };
  data(): PageData;
  mounted(): void;
  methods: PageMethods;
}

interface SearchPageComponent {
  mixins: PageComponent[];
  data(): SearchPageData;
  methods: SearchPageMethods;
}

interface SavePageComponent {
  mixins: PageComponent[];
  data(): SavePageData;
  mounted(): void;
  methods: SavePageMethods;
}

interface EditPageComponent {
  mixins: PageComponent[];
  data(): EditPageData;
  mounted(): void;
  methods: EditPageMethods;
}

interface ViewPageComponent {
  mixins: PageComponent[];
  data(): ViewPageData;
  mounted(): void;
  methods: ViewPageMethods;
}

export const Page: PageComponent;

export const SearchPage: SearchPageComponent;

export const SavePage: SavePageComponent;

export const EditPage: EditPageComponent;

export const ViewPage: ViewPageComponent;

export default {
  Page,
  SearchPage,
  SavePage,
  EditPage,
  ViewPage
};