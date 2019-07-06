import request from '@/utils/request';

export async function login(params) {
  return request('/api/user/login', {
    method: 'POST',
    body: params,
  });
}

export async function authinfo() {
  return request('/api/user/info');
}

export async function logout(params) {
  const { id } = params;
  return request(`/api/user/logout/${id}`);
}