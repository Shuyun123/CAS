import { authinfo } from '@/services/user';
import { getQueryString, updateCookie } from '@/utils/utils';

export default {
  namespace: 'global',

  state: {
    collapsed: false,
    notices: [],
    loadedAllNotices: false,
  },

  effects: {
    *caslogin(_, { call }) {
      setAuthority('user');
      reloadAuthorized();
      yield call(authinfo);
    },
  },

  reducers: {
  },

  subscriptions: {
    setup({ history, dispatch}) {
      dispatch({ type: 'caslogin' });
      // Subscribe history(url) change, trigger `load` action if pathname is `/`
      return history.listen(({ pathname, search }) => {
        if (typeof window.ga !== 'undefined') {
          window.ga('send', 'pageview', pathname + search);
        }
      });
    },
  },
};
