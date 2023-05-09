<template>
    <div class="">
        <el-button @click="toggleDebugUI()">debugui</el-button>
        <div class="flex mgb20 mgt20">
            <div class="flex flex-center">
                <a
                    href="#"
                    onclick="history.back(); return false;"
                    style="font-size: 16px"
                >
                    <i class="el-icon-back"></i>

                    Back To Snippets List
                </a>
            </div>
        </div>

        <!-- top edit bar -->
        <div class="mgb20">
            <div class="flex">
                <el-input v-model="title" style="width: 30%"></el-input>
            </div>
            <!-- edit snippet bar -->
            <div>
                <el-button @click="saveArticle">save</el-button>
                <el-button @click="addNewSnippet">+</el-button>
                <el-button @click="removeSnippet">-</el-button>
                <el-button @click="upOrDownASnippet('previous')">up</el-button>
                <el-button @click="upOrDownASnippet('next')">down</el-button>
            </div>
        </div>
        <div v-if="debugui">{{ selectedSnippetId }}</div>
        <!-- snippets -->
        <div>
            <!-- rows -->
            <div
                :id="formAnIdForSnippetDiv(snippet)"
                @click="focusSnippet($event)"
                v-for="snippet in snippets"
                :key="snippet.id"
                class="mgb20"
            >
                <div v-if="debugui">
                    <div class="mgr20">id: {{ snippet.id }}</div>

                    <div>order: {{ snippet.order }}</div>
                </div>

                <!-- code edit bar -->
                <!-- containing a lang selection and run code button -->
                <div>
                    <el-select
                        size="small"
                        v-model="snippet.lang"
                        @change="handleOptionChange(snippet)"
                    >
                        <el-option value="java" label="java"></el-option>
                        <el-option value="kotlin" label="kotlin"></el-option>
                        <el-option
                            value="javascript"
                            label="javascript"
                        ></el-option>
                        <el-option value="cpp" label="c/cpp"></el-option>
                        <el-option value="html" label="html"></el-option>
                    </el-select>

                    <el-button size="small" @click="runCode">run</el-button>
                </div>

                <!-- coding area -->
                <!-- containing the code editor and rich text editor -->
                <div class="flex" :style="`height: ${snippetHeight}px;`">
                    <!-- code -->
                    <div style="height: 100%" class="flexg1">
                        <!-- code editor -->
                        <div
                            :id="formAnIdForEditorDiv(snippet)"
                            :style="`width: 100%; height: ${editorHeight}px; border: 1px solid`"
                        ></div>
                    </div>

                    <!-- desc -->
                    <div style="height: 100%" class="flexg1">
                        <Tinymce
                            :ref="formAnIdForRichTextEditor(snippet)"
                            v-model="snippet.description"
                            :height="`${richtextHeight}px`"
                        />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Vue from 'vue'
import Tinymce from '@/components/Tinymce'
import loader from '@monaco-editor/loader'
import {
    createSnippet,
    deleteSnippet,
    getArticle,
    listSnippetsForArticle,
    updateArticle,
} from '@/api/article'
import { exchangeOrder, updateLanguageForSnippet } from '@/api/snippets'
export default {
    components: { Tinymce },
    beforeRouteLeave(to, from, next) {
        document.removeEventListener('keydown', this.handleKeyDown)
        next()
    },
    beforeRouteEnter(to, from, next) {
        next((vm) => {
            vm.$nextTick(() => {
                document.addEventListener('keydown', vm.handleKeyDown)
            })
        })
    },
    data() {
        return {
            debugui: false,
            snippetHeight: 250,
            editorHeight: 0,
            richtextHeight: 0,
            editorWidth: '500px',
            richtextWidth: '250px',
            articleId: this.$route.query.articleId,
            title: '',
            language: 'java',
            editors: [],
            monaco: undefined,
            monacoeditor: undefined,
            mode: this.$route.query.mode,
            description: '',
            onlineCodeRunningOutput: '',
            snippets: [],
            selectedSnippetId: -1,
        }
    },
    computed: {},
    watch: {},

    updated() {},
    async created() {},
    mounted() {
        this.calculateEditorHeight()
        loader.init().then((monaco) => {
            this.monaco = monaco
            this.init()
        })
    },

    methods: {
        test() {
            // var ecm = this.getARichTextById(
            //     this.formAnIdForRichTextEditor({ id: 50 }),
            // )
            // ecm.initTinymce()
            // this.setContentToARichText(
            //     ecm,
            //     'alkhg;lajsdfashglk;asjdf;aslkdjfl;asjdlkf',
            // )
        },
        formAnIdForRichTextEditor(snippet) {
            return `richText${snippet.id}`
        },
        getARichTextById(richTextId) {
            return this.$refs[richTextId][0]
        },
        setContentToARichText(richText, value) {
            richText.setContent(value)
        },
        getContentToARichText(richText) {
            return richText.getContent()
        },
        calculateEditorHeight() {
            var richTextYBorderSize = 3
            var richTextToolbarHeight = 64
            this.richtextHeight =
                this.snippetHeight - richTextToolbarHeight - richTextYBorderSize

            var editorToolbarHeight = 0
            this.editorHeight = this.snippetHeight - editorToolbarHeight
        },
        toggleDebugUI() {
            this.test()
            this.debugui = !this.debugui
        },
        getAEditorById(editorId) {
            // find from editors
            var editor = this.editors.find((editor) => {
                return editor.id === editorId
            })
            return editor
        },
        removeAnEditor(editorId) {
            // find from editors
            var index = this.editors.findIndex((editor) => {
                return editor.id === editorId
            })
            if (index == -1) {
                throw new Error('Editor does not exist')
            }
            this.editors.splice(index, 1)
        },
        getCodeFromEditor(editor) {
            return editor.editor.getValue()
        },
        setCodeToEditor(editor, code) {
            return editor.editor.setValue(code)
        },
        setLangForEditor(monaco, editor, lang) {
            monaco.editor.setModelLanguage(editor.editor.getModel(), lang)
        },
        formAnIdForEditorDiv(snippet) {
            return `editor-${snippet.id}`
        },

        // render an editor accroding to a snippet, invoked every time a content or lang of a snippet changed, also when the page is inited
        renderMonaco(snippet) {
            var editorId = this.formAnIdForEditorDiv(snippet)
            // see if we have render it
            var editor = this.getAEditorById(editorId)
            // render a new editor
            var code = snippet.content
            var lang = snippet.lang
            if (!editor) {
                // setup lang
                const editorOptions = {
                    language: lang,
                    minimap: { enabled: true },
                }

                var standaloneeditor = this.monaco.editor.create(
                    document.getElementById(editorId),
                    editorOptions,
                )

                // setup code
                standaloneeditor.setValue(code)

                this.editors.push({ id: editorId, editor: standaloneeditor })
            } else {
                code = this.getCodeFromEditor(editor)
                // simply update the code and lang of editor
                this.setCodeToEditor(editor, code)
                this.setLangForEditor(this.monaco, editor, lang)
            }
        },
        // save article, mainly title and string value of snippet
        saveArticle() {
            var articleId = this.articleId
            var title = this.title

            // update the content attr of snippet according to editors
            this.snippets.forEach((snippet) => {
                var editorId = this.formAnIdForEditorDiv(snippet)
                var editor = this.getAEditorById(editorId)
                if (editor) {
                    var code = this.getCodeFromEditor(editor)
                    snippet.content = code
                }
            })

            // request for update
            var snippets = this.snippets
            var updateArticleRequest = {
                title,
                snippets,
            }
            updateArticle(articleId, updateArticleRequest).then((resp) => {
                this.$notify({
                    title: 'success',
                    message: 'Article Saved',
                    type: 'success',
                })
            })
        },
        swapObjectsByIds(array, id1, id2) {
            // Find the indexes of the objects in the array
            const index1 = array.findIndex((obj) => obj.id == id1)
            const index2 = array.findIndex((obj) => obj.id == id2)

            // Swap the objects at the found indexes
            const temp = array[index1]
            Vue.set(array, index1, array[index2])
            Vue.set(array, index2, temp)

            // Return the updated array
            return array
        },
        upOrDownASnippet(upOrDown) {
            var snippetId = this.selectedSnippetId
            var sib = this.findSiblineSnippet(snippetId, upOrDown)

            if (!sib) {
                return
            }

            var sibId = sib.id
            var snippetAId = snippetId
            var snippetBId = sibId

            exchangeOrder({ snippetAId, snippetBId }).then((resp) => {
                // update the order of all snippets by snippet id according to orderMap
                var orderMap = resp.data.orderMap
                this.updateOrder(orderMap)
            })
        },
        // find sibling snippet by id
        findSiblineSnippet(snippetId, direction) {
            switch (direction) {
                case 'next':
                    return this.findNextSnippet(snippetId)
                case 'previous':
                    return this.findPreviousSnippet(snippetId)
                default:
                    throw Error()
            }
        },
        // find next or previous snippet
        findNextSnippet(snippetId) {
            var index = this.snippets.findIndex(
                (snippet) => snippet.id == snippetId,
            )

            // snippet is not in the array or array is empty
            if (index == -1) {
                return
            }

            // last snippet, return the first one to cycle
            if (index == this.snippets.length - 1) {
                // return this.snippets[0]
                return
            }

            return this.snippets[index + 1]
        },
        findPreviousSnippet(snippetId) {
            var index = this.snippets.findIndex(
                (snippet) => snippet.id == snippetId,
            )

            // snippet is not in the array or array is empty
            if (index == -1) {
                return
            }

            // first snippet, return the last one to cycle
            if (index == 0) {
                // return this.snippets[this.snippets.length - 1]
                return
            }

            return this.snippets[index - 1]
        },

        // remove the snippet seletedSnippetId points to
        removeSnippet() {
            // check if there is still any snippt
            if (this.snippets.length == 0) {
                return
            }

            var snippetId = this.selectedSnippetId

            // find out the index of selected snippet in the array
            var originalPlace = this.snippets.findIndex(
                (snippet) => snippet.id == snippetId,
            )

            var isLast = false

            // determine if this snippet is the last
            if (this.snippets.length > 0) {
                var lastSnippet = this.snippets[this.snippets.length - 1]
                if (lastSnippet.id == snippetId) {
                    isLast = true
                }
            }

            deleteSnippet(snippetId).then((res) => {
                // remove the snippet from the snippets array
                this.snippets = this.snippets.filter(
                    (snippet) => snippet.id != snippetId,
                )

                // if there is no other snippet, over
                if (this.snippets.length == 0) {
                    this.focusOnNothing()
                    return
                }

                var orderMap = res.data.orderMap
                this.updateOrder(orderMap)

                this.$nextTick(function () {
                    // focus the same place if we just removed the last one snippet
                    if (isLast) {
                        // focus on the last one
                        this.focusSnippetBySnippetId(
                            this.snippets[this.snippets.length - 1].id,
                        )
                    } else {
                        // focus the next snippet if we just removed the snippet that is not the last one
                        this.focusSnippetBySnippetId(
                            this.snippets[originalPlace].id,
                        )
                    }

                    // remove editor
                    var editorId = this.formAnIdForEditorDiv({ id: snippetId })
                    this.removeAnEditor(editorId)
                })
            })
        },
        // update order of elems according to the orderMap
        updateOrder(orderMap) {
            this.snippets.forEach((snippet) => {
                if (orderMap[snippet.id] != undefined) {
                    snippet.order = orderMap[snippet.id]
                }
            })

            // sort snippets by order
            this.snippets.sort((a, b) => {
                return a.order - b.order
            })

            this.$nextTick(function () {
                // reload all richtext
                this.snippets.forEach((snippet) => {
                    // find the richtext div
                    var richTextEditorId =
                        this.formAnIdForRichTextEditor(snippet)
                    var richText = this.getARichTextById(richTextEditorId)

                    // reload the richtext
                    richText.reload()
                })
            })

            // this.renderDescToRichText()
        },
        getAllSnippetElement() {
            // get all elements whose id starts with "snippet"
            const snippetElements = document.querySelectorAll(
                this.snippetDivSelector(),
            )
            return snippetElements
        },
        getSippetIdFromDivId(divId) {
            return divId.substring(
                divId.indexOf('snippet-') + 'snippet-'.length,
            )
        },
        formAnIdForSnippetDiv(snippet) {
            return `snippet-${snippet.id}`
        },
        snippetDivSelector(snippet) {
            return '[id^="snippet-"]'
        },
        focusSnippet(e) {
            // get snippet id from element, use closest to ensure if you click the nested elements like input or something, you can still get
            // the "snippet-" prefixed div correctly
            var target = e.target.closest(this.snippetDivSelector())

            var selectedSnippetId = this.getSippetIdFromDivId(target.id)

            this.doFocusSnippet(target, selectedSnippetId)
        },

        focusOnNothing() {
            this.selectedSnippetId = -1
        },

        focusSnippetBySnippetId(selectedSnippetId) {
            // get snippet element by id
            var target = document.getElementById(
                this.formAnIdForSnippetDiv({ id: selectedSnippetId }),
            )
            this.doFocusSnippet(target, selectedSnippetId)
        },

        doFocusSnippet(target, selectedSnippetId) {
            // highlight it
            target.classList.add('snippet-highlight')

            this.selectedSnippetId = selectedSnippetId

            // remove highlight from the rest snippet
            this.reverseHighLightSnippet(selectedSnippetId)
        },

        reverseHighLightSnippet(selectedSnippetId) {
            // remove highlight from the rest snippet
            var snippetElements = this.getAllSnippetElement()
            snippetElements.forEach((element) => {
                // get the part of the ID that follows "snippet"
                const snippetId = this.getSippetIdFromDivId(element.id)
                if (snippetId != selectedSnippetId) {
                    console.log(snippetId) // output: "123"
                    element.classList.remove('snippet-highlight')
                }
            })
        },

        addNewSnippet() {
            // find order of current focused snippet
            var snippets = this.snippets
            var selectedOrder = -1
            snippets.forEach((snippet) => {
                if (snippet.id == this.selectedSnippetId) {
                    selectedOrder = snippet.order
                }
            })

            // we are going to create the first one snippet
            var newOrder = selectedOrder < 0 ? 0 : selectedOrder + 1

            createSnippet(this.articleId, {
                content: '',
                description: '',
                lang: 'kotlin',
                order: newOrder,
            }).then((resp) => {
                var newSnippetHandle = resp.data.newSnippetHandle

                // add new snippet to snippets anyway
                var newSnippet = {
                    id: newSnippetHandle,
                    content: '',
                    description: '',
                    lang: 'kotlin',
                    order: newOrder,
                }
                this.snippets.push(newSnippet)

                // update the order of all snippets by snippet id according to orderMap
                var orderMap = resp.data.orderMap
                this.updateOrder(orderMap)

                // focus on the new snippet
                this.$nextTick(function () {
                    this.focusSnippetBySnippetId(newSnippetHandle)
                    this.renderMonaco(newSnippet)
                })
            })
        },
        handleKeyDown(event) {
            // Check if the "Ctrl" key and "S" key were both pressed
            if (event.ctrlKey && event.key === 's') {
                event.preventDefault()
                console.log('Ctrl + S pressed!')
                this.saveCode()
                // Do something else here, such as saving data or triggering an action
            }
        },
        handleOptionChange(snippet) {
            this.updateLanguageForSnippet(snippet.id, snippet.lang)
        },
        runCode() {},
        renderDescToRichText() {
            // set content to each rich text editor
            this.snippets.forEach((snippet) => {
                // generate richtext id
                var richtextId = this.formAnIdForRichTextEditor(snippet)
                console.log(this.$refs)

                // get richtext element
                var richtext = this.getARichTextById(richtextId)

                console.log('richtext: ', richtext)
                console.log('its value: ', this.getContentToARichText(richtext))

                // set content to it
                this.setContentToARichText(richtext, snippet.description)
            })
        },
        init() {
            // get article id from url
            getArticle(this.articleId)
                .then((resp) => {
                    this.title = resp.data.title
                    return listSnippetsForArticle(this.articleId)
                })
                .then((resp) => {
                    this.snippets = resp.data
                    this.snippets.sort((a, b) => a.order - b.order)

                    if (this.snippets.length > 0) {
                        this.$nextTick(function () {
                            // focus the first snippet
                            this.snippets.forEach((snippet) => {
                                this.renderMonaco(snippet)
                            })
                            // render editor to ui
                            this.focusSnippetBySnippetId(this.snippets[0].id)
                        })
                    }
                })
        },
        updateLanguageForSnippet(snippetId, lang) {
            // request backend to change language
            updateLanguageForSnippet(snippetId, lang).then((resp) => {
                this.$nextTick(function () {
                    // find the snippet
                    var snippet = this.snippets.find(
                        (snippet) => snippet.id == snippetId,
                    )
                    this.renderMonaco(snippet)
                })
            })
        },
        saveCode() {
            const code = this.standaloneeditor.getValue()

            if (this.title === '') {
                this.$notify({
                    type: 'error',
                    message: 'title is empty',
                })
                return
            }

            if (this.mode == 'create') {
                createSnippet({
                    title: this.title,
                    codeContent: code,
                    lang: this.language,
                    description: this.description,
                }).then((resp) => {
                    this.$notify({
                        type: 'success',
                        message: 'SUCCESS',
                    })
                })
            } else if (this.mode == 'edit') {
                updateArticle(this.id, {
                    title: this.title,
                    codeContent: code,
                    lang: this.language,
                    description: this.description,
                }).then((resp) => {
                    this.$notify({
                        type: 'success',
                        message: 'SUCCESS',
                    })
                })
            }
        },
    },
}
</script>
<style lang="scss" scoped>
@import '~@/styles/common-style.scss';

.snippet-highlight {
    background-color: azure;
}
</style>
