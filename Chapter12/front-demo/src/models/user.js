import { authinfo } from '@/services/user';

export default {
  namespace: 'user',

  state: {
    list: [],
    currentUser: {},
  },

  effects: {
    *authinfo({_}, { call, put }) {
      const response = yield call(authinfo);
      if(response && response != undefined){
        yield put({
          type: 'updateState',
          payload: {
            currentUser: response,
          },
        });
      }
    }
  },

  reducers: {
    updateState(state, { payload }) {
      return {
        ...state,
        ...payload,
      };
    },
  },
};
