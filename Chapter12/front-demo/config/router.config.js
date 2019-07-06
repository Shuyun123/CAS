export default [
  // user
  {
    path: '/user',
    component: '../layouts/UserLayout',
    routes: [
      { path: '/user', redirect: '/user/login' },
      { path: '/user/login', component: './User/Login' },
    ],
  },
  // app
  {
    path: '/',
    Routes: ['src/pages/Authorized'],
    authority: ['admin', 'user'],
    routes: [
      // home
      { path: '/', redirect: '/home' },
      { path: '/home', component: './Home/Index' },
    ],
  },
];
