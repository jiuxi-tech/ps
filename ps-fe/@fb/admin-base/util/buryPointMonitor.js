import Qs from 'qs'


// 纯事件埋点监听
export function eventBuryPointMonitor(_this) {
	_this = _this || app
	// _this.on('xxx', function () {
	// 	// alert(1)
	// })
	// _this.trigger('xxx')

	_this.on('monitor-login', (data) => {
		console.log('monitor-login', data)
	})
	_this.on('monitor-login-out', (data) => {
		console.log('monitor-login-out', data)
	})
}

// 挂载全局接口事件
export function mountedAppEvent(_this) {
	_this = _this || app

	_this.Qs = Qs

	// 文件下载方法
	_this.download = function (response) {

		let blob = new Blob([response.data])
		let contentDisposition = response.headers['content-disposition']

		let fileName = ''
		// 附件下载
		if (contentDisposition && contentDisposition.indexOf('filename*') != -1) {
			let file = contentDisposition.split('utf-8\'\'')[1]
			fileName = decodeURI(file)
		} else if (contentDisposition && contentDisposition.indexOf('filename=') != -1) {
			// 切割的目的是取出附件名称。例如：attachment; filename="filename.jpg"
			let file = contentDisposition.split('"')[1]
			fileName = decodeURI(file)
		}

		if (window.navigator.msSaveOrOpenBlob) {
			// console.log(2)
			navigator.msSaveBlob(blob, fileName)
		} else {
			// console.log(3)
			let link = document.createElement('a')
			link.style.display = 'none'
			link.href = window.URL.createObjectURL(blob)
			link.download = fileName
			link.click()
			window.URL.revokeObjectURL(link.href)
			// 释放内存，由于报 Failed to execute 'removeChild' on 'Node': The node to be
			// removed is not a child of this node. 的错误，暂时注释
			// document.body.removeChild(link); //下载完成移除元素
		}
	}
}
