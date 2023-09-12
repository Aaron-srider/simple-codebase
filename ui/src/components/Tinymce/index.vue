<template>
    <div
        :class="{ fullscreen: fullscreen }"
        class="tinymce-container"
        :style="{ width: containerWidth }"
    >
        <textarea :id="tinymceId" class="tinymce-textarea" />
        <div class="editor-custom-btn-container">
            <!-- <editorImage
                color="#1890ff"
                class="editor-upload-btn"
                @successCBK="imageSuccessCBK"
            /> -->
        </div>
    </div>
</template>

<script lang="ts">
import plugins from './plugins';
import toolbar from './toolbar';
import load from './dynamicLoadScript';

// why use this cdn, detail see https://github.com/PanJiaChen/tinymce-all-in-one
const tinymceCDN =
    'https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/tinymce.min.js';

import { Component, Vue, Prop, Watch } from 'vue-property-decorator';

@Component({ components: {} })
export default class TinymceComponents extends Vue {
    @Prop({
        type: String,
        default: () =>
            'vue-tinymce-' +
            new Date().getTime() +
            (Math.random() * 1000).toFixed(0),
    })
    readonly id!: string;

    @Prop({ type: String, default: '' })
    readonly value!: string;

    @Prop({
        type: Array,
        required: false,
        default: () => [
            'strikethrough alignleft aligncenter alignright outdent indent blockquote undo redo subscript superscript code codesample',
        ],
    })
    readonly toolbar!: string[];

    @Prop({ type: String, default: '' }) private readonly menubar!: string;

    @Prop({ type: [Number, String], required: false, default: 360 })
    readonly height!: number | string;

    @Prop({ type: [Number, String], required: false, default: 'auto' })
    readonly width!: number | string;

    hasChange = false;
    hasInit = false;
    tinymceId!: string;
    fullscreen = false;

    languageTypeList: Record<string, string> = {
        en: 'en',
        zh: 'zh_CN',
        es: 'es_MX',
        ja: 'ja',
    };

    get containerWidth() {
        const width = this.width;
        if (/^[\d]+(\.[\d]+)?$/.test(width as string)) {
            // matches `100`, `'100'`
            return `${width}px`;
        }
        return width;
    }

    @Watch('value')
    onValueChanged(val) {
        if (!this.hasChange && this.hasInit) {
            this.$nextTick(() =>
                // @ts-ignore
                window.tinymce.get(this.tinymceId).setContent(val || ''),
            );
        }
    }

    created() {
        this.tinymceId = this.id;
    }

    mounted() {
        this.init();
    }

    activated() {
        // @ts-ignore
        if (window.tinymce) {
            this.initTinymce();
        }
    }

    deactivated() {
        this.destroyTinymce();
    }

    destroyed() {
        this.destroyTinymce();
    }

    init() {
        // dynamic load tinymce from cdn
        load(tinymceCDN, (err) => {
            if (err) {
                this.$message.error(err.message);
                return;
            }
            this.$emit('inited');
            this.initTinymce();
        });
    }

    initTinymce() {
        const _this = this;
        // @ts-ignore
        window.tinymce.init({
            selector: `#${this.tinymceId}`,
            language: this.languageTypeList['en'],
            height: this.height,
            body_class: 'panel-body ',
            object_resizing: false,
            toolbar: this.toolbar.length > 0 ? this.toolbar : toolbar,
            menubar: this.menubar,
            plugins: plugins,
            end_container_on_empty_block: true,
            powerpaste_word_import: 'clean',
            code_dialog_height: 450,
            code_dialog_width: 1000,
            advlist_bullet_styles: 'square',
            advlist_number_styles: 'default',
            imagetools_cors_hosts: ['www.tinymce.com', 'codepen.io'],
            default_link_target: '_blank',
            link_title: false,
            nonbreaking_force_tab: true, // inserting nonbreaking space &nbsp; need Nonbreaking Space Plugin
            init_instance_callback: (editor) => {
                if (_this.value) {
                    editor.setContent(_this.value);
                }
                _this.hasInit = true;
                editor.on('NodeChange Change KeyUp SetContent', () => {
                    this.hasChange = true;
                    this.$emit('input', editor.getContent());
                });
            },
            setup(editor) {
                editor.on('FullscreenStateChanged', (e) => {
                    _this.fullscreen = e.state;
                });
            },
            // it will try to keep these URLs intact
            // https://www.tiny.cloud/docs-3x/reference/configuration/Configuration3x@convert_urls/
            // https://stackoverflow.com/questions/5196205/disable-tinymce-absolute-to-relative-url-conversions
            convert_urls: false,
            // 整合七牛上传
            // images_dataimg_filter(img) {
            //   setTimeout(() => {
            //     const $image = $(img);
            //     $image.removeAttr('width');
            //     $image.removeAttr('height');
            //     if ($image[0].height && $image[0].width) {
            //       $image.attr('data-wscntype', 'image');
            //       $image.attr('data-wscnh', $image[0].height);
            //       $image.attr('data-wscnw', $image[0].width);
            //       $image.addClass('wscnph');
            //     }
            //   } 0);
            //   return img
            // }
            // images_upload_handler(blobInfo, success, failure, progress) {
            //   progress(0);
            //   const token = _this.$store.getters.token;
            //   getToken(token).then(response => {
            //     const url = response.data.qiniu_url;
            //     const formData = new FormData();
            //     formData.append('token', response.data.qiniu_token);
            //     formData.append('key', response.data.qiniu_key);
            //     formData.append('file', blobInfo.blob(), url);
            //     upload(formData).then(() => {
            //       success(url);
            //       progress(100);
            //     })
            //   }).catch(err => {
            //     failure('出现未知问题，刷新页面，或者联系程序员')
            //     console.log(err);
            //   });
            // }
        });

        console.log(`load mce ${this.tinymceId}`);
    }

    reload() {
        this.destroyTinymce();
        this.initTinymce();
    }

    destroyTinymce() {
        // @ts-ignore
        const tinymce = window.tinymce.get(this.tinymceId);
        if (this.fullscreen) {
            tinymce.execCommand('mceFullScreen');
        }

        if (tinymce) {
            tinymce.destroy();
        }
    }

    setContent(value) {
        // @ts-ignore
        window.tinymce.get(this.tinymceId).setContent(value);
    }

    getContent() {
        // @ts-ignore
        return window.tinymce.get(this.tinymceId).getContent();
    }

    imageSuccessCBK(arr) {
        arr.forEach((v) =>
            // @ts-ignore
            window.tinymce
                .get(this.tinymceId)
                .insertContent(`<img class="wscnph" src="${v.url}" >`),
        );
    }
}
</script>

<style lang="scss" scoped>
.tinymce-container {
    position: relative;
    line-height: normal;
}

.tinymce-container {
    ::v-deep {
        .mce-fullscreen {
            z-index: 10000;
        }
    }
}

.tinymce-textarea {
    visibility: hidden;
    z-index: -1;
}

.editor-custom-btn-container {
    position: absolute;
    right: 4px;
    top: 4px;
    /*z-index: 2005;*/
}

.fullscreen .editor-custom-btn-container {
    z-index: 10000;
    position: fixed;
}

.editor-upload-btn {
    display: inline-block;
}
</style>
