<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top" style="padding-left: 6px; padding-right: 6px">
			<fb-form :label-width="160" ref="fbform" style="height: 100%">
				<div id="iframeEditor"></div>
			</fb-form>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>


	export default {
		name: 'editor',
		// 接收父组件的传参
		props: {
			param: {
				type: Object,
				require: false
			},
			parentPage: {
				type: Object,
				default: null
			}
		},
		// 组件
		components: {
			// 'component-a': ComponentA,
		},
		// 创建方法
		created() {
			// 记录原来的默认值，用于表单重置
		},
		// 初始化方法
		mounted() {
			this.init();
		},
		data() {
			return {

				// 编辑页面初始化
				docEditor: {},
				dataInsertImage: '',
				config: {},
				dataCompareFile: {},
				dataMailMergeRecipients: {},
				histArray: [],
				usersForMentions: {},


				paramData: {
					personId: '1478708138734518272',
					personName: 'admin',
					deptId: '1424558870621061124',
					attachId: '1478658068727201792',
					attachName: '涉爆粉尘企业风险监测预警系统接入规范-tx20211027(1).docx',
					savePath: '/D1/09/27/b3d4042172294b32a1e3ef3629fdc00f.docx',
					action: 'edit',
					type: 'desktop',
					actionLink: null,
					uid: '1',
					ulang: 'zh',
				},
			}
		},

		// 方法
		methods: {
			init() {

				let that = this;

				// 发送请求，初始化编辑页面
				this.$svc.common.document.initEditor(that.paramData).then(res => {

					that.config = res.model;
					that.dataInsertImage = res.dataInsertImage;
					that.dataCompareFile = res.dataCompareFile;
					that.dataMailMergeRecipients = res.dataMailMergeRecipients;
					that.histArray = res.fileHistory;
					that.usersForMentions = res.usersForMentions;

					if (that.config.editorConfig.user.name == "") {
						that.config.editorConfig.user.name = "佚名";
					}

					that.config.width = "100%";
					that.config.height = "100%";
					that.config.events = {
						"onAppReady": that.onAppReady,
						"onDocumentStateChange": that.onDocumentStateChange,
						'onRequestEditRights': that.onRequestEditRights,
						"onError": that.onError,
						"onOutdatedVersion": that.onOutdatedVersion,
						"onMakeActionLink": that.onMakeActionLink,
						"onMetaChange": that.onMetaChange,
						"onRequestInsertImage": that.onRequestInsertImage,
						"onRequestCompareFile": that.onRequestCompareFile,
						"onRequestMailMergeRecipients": that.onRequestMailMergeRecipients,
					};

					if (that.histArray[0] && that.histArray[1]) {
						that.config.events['onRequestHistory'] = that.onRequestHistory;
						that.config.events['onRequestHistoryData'] = that.onRequestHistoryData;
						that.config.events['onRequestHistoryClose'] = that.onRequestHistoryClose;
					}

					if (that.usersForMentions) {
						that.config.events['onRequestUsers'] = that.onRequestUsers;
						that.config.events['onRequestSendNotify'] = that.onRequestSendNotify;
					}

					// 先加载api.js
					that.loadOnlyOfficeAPI(res.docserviceApiUrl).then(() => {
						if (window.addEventListener) {
							window.addEventListener("load", that.сonnectEditor(), {passive: false});
						} else if (window.attachEvent) {
							window.attachEvent("load", that.сonnectEditor(), {passive: false});
						}
					})
				});
			},

			сonnectEditor() {
				this.docEditor = new DocsAPI.DocEditor("iframeEditor", this.config);
			},

			/**
			 * 当用户尝试通过单击“版本历史”按钮来显示文档版本历史时调用的函数。
			 * 要显示文档版本历史，必须调用refreshHistory方法。如果未声明该方法和onRequestHistoryData方法，则不会显示版本历史记录按钮。
			 */
			onRequestHistory() {
				this.docEditor.refreshHistory(JSON.parse(this.histArray[0]));
			},

			/**
			 * 当用户尝试单击文档版本历史记录中的特定文档版本时调用的函数。
			 * 要显示与特定文档版本相对应的更改，必须调用setHistoryData方法。文档版本号在数据参数中发送。如果未声明该方法和onRequestHistory方法，则不会显示版本历史记录按钮。
			 */
			onRequestHistoryData(event) {
				let ver = event.data;
				this.docEditor.setHistoryData(JSON.parse(this.histArray[1])[ver - 1]);
			},

			/**
			 * 当用户试图通过单击关闭历史记录按钮查看文档版本历史记录返回到文档时调用的函数。
			 * 调用该函数时，必须在编辑模式下再次初始化编辑器。如果未声明该方法，则不会显示关闭历史记录按钮。
			 */
			onRequestHistoryClose() {
				document.location.reload();
			},

			/**
			 * 当评论者可以选择其他用户在评论中提及时调用的函数。要设置用户列表，必须调用setUsers方法。
			 */
			onRequestUsers() {
				this.docEditor.setUsers({
					"users": this.usersForMentions
				});
			},

			/**
			 * 在评论中提到用户时调用的函数。要提及的用户列表应通过setUsers方法完成。
			 * 消息和电子邮件列表在数据参数中发送。评论数据在data.actionLink参数中接收，然后必须在配置中用作editorConfig.actionLink参数的值。
			 *
			 * 在 5.4 版本中，只有在设置了onRequestUsers事件时才能使用onRequestSendNotify事件。从 5.5 版开始，onRequestSendNotify和onRequestUsers之间没有这种依赖关系——两者都可以独立设置。
			 */
			onRequestSendNotify(event) {
				let actionLink = JSON.stringify(event.data.actionLink);
				let replaceLink = this.replaceActionLink(location.href, actionLink);
				this.docEditor.setActionLink(replaceLink);
			},

			innerAlert(message) {
				if (console && console.log)
					console.log(message);
			},

			/**
			 * 当应用程序加载到浏览器时调用的函数。
			 */
			onAppReady() {
				this.innerAlert("Document editor ready");
			},

			/**
			 * 修改文档时调用的函数。当当前用户正在编辑文档时使用参数：{"data": true}调用它，当当前用户的更改发送到文档编辑服务时使用参数：{"data": false}调用它。
			 */
			onDocumentStateChange(event) {
				let title = document.title.replace(/\*$/g, "");
				document.title = title + (event.data ? "*" : "");
			},

			/**
			 * 当用户尝试通过单击“编辑文档”按钮将文档从查看模式切换到编辑模式时调用的函数。调用该函数时，必须在编辑模式下再次初始化编辑器。如果未声明该方法，则不会显示“编辑”按钮。
			 */
			onRequestEditRights() {
				location.href = location.href.replace(RegExp("mode=view\&?", "i"), "");
			},

			/**
			 * 发生错误或其他特定事件时调用的函数。错误消息在数据参数中发送。
			 */
			onError(event) {
				if (event) this.innerAlert(event.data);
			},

			/**
			 * 显示错误后调用的函数，当使用旧的document.key值打开文档进行编辑时，该值用于编辑以前的文档版本并成功保存。当这个事件被调用时，编辑器必须用一个新的document.key重新初始化。
			 */
			onOutdatedVersion(event) {
				location.reload(true);
			},

			/**
			 * 当用户试图获取打开包含书签的文档的链接时调用的函数，滚动到书签位置。
			 * 要设置书签链接，您必须调用setActionLink方法。书签数据在data参数中接收，然后必须在配置中用作editorConfig.actionLink参数的值。如果未声明该方法，则不会显示“获取链接”按钮。
			 */
			onMakeActionLink(event) {
				let actionData = event.data;
				let linkParam = JSON.stringify(actionData);
				this.docEditor.setActionLink(this.replaceActionLink(location.href, linkParam));
			},

			replaceActionLink(href, linkParam) {
				var link;
				var actionIndex = href.indexOf("&actionLink=");
				if (actionIndex != -1) {
					var endIndex = href.indexOf("&", actionIndex + "&actionLink=".length);
					if (endIndex != -1) {
						link = href.substring(0, actionIndex) + href.substring(endIndex) + "&actionLink=" + encodeURIComponent(linkParam);
					} else {
						link = href.substring(0, actionIndex) + "&actionLink=" + encodeURIComponent(linkParam);
					}
				} else {
					link = href + "&actionLink=" + encodeURIComponent(linkParam);
				}
				return link;
			},

			/**
			 * 通过meta命令更改文档的元信息时调用的函数。文档的名称在data.title参数中发送。在最喜欢的图标高亮状态在发送data.favorite参数。
			 * 当用户点击收藏夹图标时， setFavorite方法被调用来更新信息有关的收藏图标高亮状态。如果未声明该方法，则收藏夹图标不会更改。
			 */
			onMetaChange(event) {
				let favorite = !!event.data.favorite;
				let title = document.title.replace(/^\☆/g, "");
				document.title = (favorite ? "☆" : "") + title;// ☆
				this.docEditor.setFavorite(favorite);
			},


			/**
			 * 当用户尝试通过单击“存储中的图像”按钮插入图像时调用的函数。图像插入的类型在参数data.c 中指定。
			 * 要将图像插入到文件中，必须使用指定的命令调用insertImage方法。如果未声明该方法，则不会显示来自存储按钮的图像。
			 */
			onRequestInsertImage(event) {
				const temp = Object.assign({}, {"c": event.data.c}, this.dataInsertImage);
				this.docEditor.insertImage(temp);
			},

			/**
			 * 当用户尝试通过单击Document from Storage按钮选择要比较的文档时调用的函数。
			 * 要选择一个文档进行比较，必须调用setRevisedFile方法。如果未声明该方法，则不会显示来自存储按钮的文档
			 *
			 * ** 仅适用于 ONLYOFFICE 企业版和 ONLYOFFICE 开发版 **
			 */
			onRequestCompareFile() {
				this.docEditor.setRevisedFile(this.dataCompareFile);
			},

			/**
			 * 当用户尝试通过单击邮件合并按钮选择收件人数据时调用的函数。
			 * 要选择收件人数据，您必须调用setMailMergeRecipients方法。如果未声明该方法，则不会显示邮件合并按钮。
			 */
			onRequestMailMergeRecipients() {
				this.docEditor.setMailMergeRecipients(this.dataMailMergeRecipients);
			},


			// 关闭
			handleClose() {
				let param = {};
				this.closeTpDialog(param);
			},

			/**
			 * 动态加载app.js
			 */
			loadOnlyOfficeAPI(src) {
				// 动态加载js
				return new Promise((resolve, reject) => {
					const script = document.createElement('script')
					script.type = 'text/javascript'
					script.src = src
					document.head.appendChild(script)
					script.onload = () => {
						resolve()
					}
					script.onerror = () => {
						reject()
					}
				})
			}
		}
	}
</script>

<style lang="less" scoped>

</style>
