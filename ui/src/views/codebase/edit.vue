<template>
    <div class="">
        <el-button @click="changeLanguage('javascript')">js</el-button>
        <el-button @click="changeLanguage('java')">java</el-button>
        <el-button @click="saveCode">save</el-button>
        <div id="editor" style="width: 500px; height: 500px"></div>
    </div>
</template>

<script>
import loader from '@monaco-editor/loader'
import { getSnippetById, updateSnippet } from '@/api/snippets.js'
export default {
    components: {},
    data() {
        return {
            language: 'javascript',
            monacoeditor: undefined,
        }
    },
    computed: {},
    watch: {},
    async created() {
        this.init()
        var snippetId = this.$route.query.snippetId
        if (snippetId == undefined) {
            throw Error('page init error')
        }

        this.snippetId = snippetId
        getSnippetById(snippetId).then((resp) => {
            this.standaloneeditor.setValue(resp.data.codeContent)
            this.changeLanguage(resp.data.lang)
        })
    },

    methods: {
        async init() {
            loader.init().then((monaco) => {
                const editorOptions = {
                    language: 'java',
                    minimap: { enabled: true },
                }
                var monacoeditor = monaco.editor
                var standaloneeditor = monacoeditor.create(
                    document.getElementById('editor'),
                    editorOptions,
                )

                this.standaloneeditor = standaloneeditor

                this.monacoeditor = monacoeditor
            })
        },
        changeLanguage(lang) {
            this.monacoeditor.setModelLanguage(
                this.standaloneeditor.getModel(),
                lang,
            )
        },
        saveCode() {
            const code = this.standaloneeditor.getValue()
            updateSnippet(this.snippetId, {
                title: 'default title',
                codeContent: code,
                lang: this.language,
            }).then((resp) => {
                this.$notify({
                    type: 'success',
                    message: 'SUCCESS',
                })
            })
            // do something with the code
        },
    },
}
</script>
<style lang="scss" scoped></style>
