
const logconstant = {

    moduleCodeData: [
        {value: 'login', label: '登陆'},
        {value: 'user', label: '用户'},
    ],

    appNameData: [
        {value: '', label: '系统'},
    ],

    operterTypeData: [
        {value: 'login', label: '登陆'},
        {value: 'logout', label: '登出'},
        {value: 'unlocked', label: '解锁'},
        {value: 'locked', label: '锁定'},
        //resetpwd
        {value: 'resetpwd', label: '重置密码'},
        //disabled_account
        {value: 'disabled_account', label: '停用账号'},
        //enabled_account
        {value: 'enabled_account', label: '启用账号'},

        // {value: 'add', label: '新增'},
        // {value: 'delete', label: '删除'},
        // {value: 'edit', label: '修改'},
        // {value: 'query', label: '查询'},
        // {value: 'pass', label: '通过'},
        // {value: 'unpass', label: '未通过'}
    ],

    categoryData: [
        {value: '0', label: '政府'},
        {value: '1', label: '企业'},
        {value: '2', label: '中介'},
        {value: '3', label: '其它'},
    ],

    sourceData: [
        {value: '1', label: '前端'},
        {value: '2', label: '后端'}
    ]
}

let install = logconstant.install = (adapter, opt) => {
    opt = opt || {};
    let $logconstant = opt.logconstant ? opt.logconstant : logconstant
    app.$logconstant = adapter.prototype.$logconstant = $logconstant
}

export default {
    logconstant,
    install
}