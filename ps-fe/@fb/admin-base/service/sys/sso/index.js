// 创建应用service
export default {
	user: {
        status(){
            return app.service.get('/sso/user/status')
        },
        clients(){
            return app.service.get('/sso/user/clients')
        },
        roles(){
            return app.service.get('/sso/user/roles')
        },
        resources(){
            return app.service.get('/sso/user/resources')
        },
        logout(){
            return app.service.post('/sso/user/logout')
        }
    },
	admin: {
		session: {
			// 获取keycloak的全局会话列表
			list(params) {
				return app.service.get('/sso/admin/session/list', params)
			},
			// 强制注销keycloak的全局会话
			logout(sessionId) {
				return app.service.post('/sso/admin/session/logout', { sessionId })
			},
			// 批量强制注销会话
			batchLogout(sessionIds) {
				return app.service.post('/sso/admin/session/batch-logout', { sessionIds })
			}
		},
		client: {
			// 获取keycloak的客户端列表
			list(params) {
				return app.service.get('/sso/admin/client/list', {params})
			},
			// 获取单个客户端信息
			get(clientId) {
				return app.service.get('/sso/admin/client/get',{ params:{clientId} })
			},
			// 保存客户端信息（新增或更新）
			save(params) {
				return app.service.post('/sso/admin/client/save', params)
			},
			// 删除客户端
			delete(clientId) {
				return app.service.delete('/sso/admin/client/delete', { clientId })
			}
		},
		userEvent: {
			// 获取keycloak用户事件列表
			list(params) {
				return app.service.get('/sso/admin/user-event/list', {params})
			}
		},
		adminEvent: {
			// 获取keycloak管理员事件列表
			list(params) {
				return app.service.get('/sso/admin/admin-event/list', {params})
			}
		}
	}
}