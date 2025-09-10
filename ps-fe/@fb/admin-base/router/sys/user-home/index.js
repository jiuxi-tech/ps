export default [
  {
    path: '/sys/user-home',
    name: 'UserHome',
    component: () => import('@fb/admin-base/views/sys/user-home/list.vue'),
    meta: {
      title: '用户首页',
      icon: 'home',
      keepAlive: true,
      requiresAuth: true
    }
  },
  {
    path: '/sys/user-home/profile',
    name: 'UserProfile',
    component: () => import('@fb/admin-base/views/sys/user-home/profile.vue'),
    meta: {
      title: '个人信息',
      icon: 'user',
      keepAlive: true,
      requiresAuth: true
    }
  }
]