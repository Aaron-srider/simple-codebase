<template>
    <div class="" v-loading="isPageLoading">
        <page-header :title="'Edit'"></page-header>
        <page-content>
            <!-- <el-button @click="toggleDebugUI()">debugui</el-button> -->
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
                title
                <div class="flex">
                    <el-input
                        class="flexg1"
                        v-model="title"
                        style="width: 30%"
                    ></el-input>
                    <el-button
                        class="mgl20"
                        size="mini"
                        type="primary"
                        @click="saveArticle"
                    >
                        save
                    </el-button>
                </div>
                <!-- edit snippet bar -->
                <div>
                    <el-button
                        size="mini"
                        style="border-radius: 0px; margin: 0"
                        @click="addNewSnippet"
                    >
                        <i class="el-icon-plus"></i>
                    </el-button>
                    <el-button
                        size="mini"
                        style="border-radius: 0px; margin: 0"
                        @click="removeSnippet"
                    >
                        <i class="el-icon-delete"></i>
                    </el-button>
                    <el-button
                        size="mini"
                        style="border-radius: 0px; margin: 0"
                        @click="upOrDownASnippet('previous')"
                    >
                        <i class="el-icon-top"></i>
                    </el-button>
                    <el-button
                        size="mini"
                        style="border-radius: 0px; margin: 0"
                        @click="upOrDownASnippet('next')"
                    >
                        <i class="el-icon-bottom"></i>
                    </el-button>
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
                            <el-option
                                value="kotlin"
                                label="kotlin"
                            ></el-option>
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
                                :style="`width: 100%; height: ${editorHeight}px; border: 1px solid #ced4da`"
                            ></div>
                        </div>

                        <!-- desc -->
                        <div style="height: 100%" class="flexg1">
                            <Tinymce
                                @inited="handleRichtextInited"
                                :ref="formAnIdForRichTextEditor(snippet)"
                                v-model="snippet.description"
                                :height="`${richtextHeight}px`"
                            />
                        </div>
                    </div>
                </div>
            </div>
        </page-content>
        <page-footer></page-footer>
    </div>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Tinymce from '@/components/Tinymce/index.vue';
import loader from '@monaco-editor/loader';
import PageHeader from '@/views/common/page-header.vue';
import PageContent from '@/views/common/page-content.vue';
import PageFooter from '@/views/common/page-footer.vue';
import Client from '@/request/client';

@Component({ components: { Tinymce, PageContent, PageHeader, PageFooter } })
export default class EditView extends Vue {
    tinyMceCompnentLoading = true;
    dataLoading = false;
    testheight: any = 19 * 10;
    testmonacoeditor: any = null;
    debugui = false;
    snippetHeight = 250;
    editorHeight = 0;
    richtextHeight = 0;
    editorWidth = '500px';
    richtextWidth = '250px';
    articleId = this.$route.query.articleId;
    title = '';
    language = 'java';
    editors: any[] = [];
    monaco: any = null;
    monacoeditor: any = null;
    mode: any = this.$route.query.mode;
    description = '';
    onlineCodeRunningOutput: any = '';
    snippets: any[] = [];
    selectedSnippetId: any = -1;
    get isPageLoading() {
        return (
            this.tinyMceCompnentLoading === true || this.dataLoading === true
        );
    }

    mounted() {
        this.dataLoading = true;
        this.calculateEditorHeight();
        loader.init().then((monaco) => {
            this.monaco = monaco;
            this.init();
        });
    }
    handleRichtextInited() {
        this.tinyMceCompnentLoading = false;
    }

    test() {
        // var ecm = this.getARichTextById(
        //     this.formAnIdForRichTextEditor({ id: 50 }),
        // )
        // ecm.initTinymce()
        // this.setContentToARichText(
        //     ecm,
        //     'alkhg;lajsdfashglk;asjdf;aslkdjfl;asjdlkf',
        // )
    }

    formAnIdForRichTextEditor(snippet) {
        return `richText${snippet.id}`;
    }

    getARichTextById(richTextId) {
        return this.$refs[richTextId][0];
    }

    setContentToARichText(richText, value) {
        richText.setContent(value);
    }

    getContentToARichText(richText) {
        return richText.getContent();
    }

    calculateEditorHeight() {
        var richTextYBorderSize = 3;
        var richTextToolbarHeight = 64;
        this.richtextHeight =
            this.snippetHeight - richTextToolbarHeight - richTextYBorderSize;

        var editorToolbarHeight = 0;
        this.editorHeight = this.snippetHeight - editorToolbarHeight;
    }

    toggleDebugUI() {
        this.test();
        this.debugui = !this.debugui;
    }

    getAEditorById(editorId) {
        // find from editors
        var editor = this.editors.find((editor) => {
            return editor.id === editorId;
        });
        return editor;
    }

    removeAnEditor(editorId) {
        // find from editors
        var index = this.editors.findIndex((editor) => {
            return editor.id === editorId;
        });
        if (index == -1) {
            throw new Error('Editor does not exist');
        }
        this.editors.splice(index, 1);
    }

    getCodeFromEditor(editor) {
        return editor.editor.getValue();
    }

    setCodeToEditor(editor, code) {
        return editor.editor.setValue(code);
    }

    setLangForEditor(monaco, editor, lang) {
        monaco.editor.setModelLanguage(editor.editor.getModel(), lang);
    }

    formAnIdForEditorDiv(snippet) {
        return `editor-${snippet.id}`;
    }

    // render an editor accroding to a snippet, invoked every time a content or lang of a snippet changed, also when the page is inited
    renderMonaco(snippet) {
        var editorId = this.formAnIdForEditorDiv(snippet);
        // see if we have render it
        var editor = this.getAEditorById(editorId);
        // render a new editor
        var code = snippet.content;
        var lang = snippet.lang;
        if (!editor) {
            // setup lang
            const editorOptions = {
                language: lang,
                minimap: { enabled: true },
            };

            var standaloneeditor = this.monaco.editor.create(
                document.getElementById(editorId),
                editorOptions,
            );

            // setup code
            standaloneeditor.setValue(code);

            this.editors.push({ id: editorId, editor: standaloneeditor });
        } else {
            code = this.getCodeFromEditor(editor);
            // simply update the code and lang of editor
            this.setCodeToEditor(editor, code);
            this.setLangForEditor(this.monaco, editor, lang);
        }
    }

    // save article, mainly title and string value of snippet
    saveArticle() {
        var articleId = this.articleId;
        var title = this.title;

        // update the content attr of snippet according to editors
        this.snippets.forEach((snippet) => {
            var editorId = this.formAnIdForEditorDiv(snippet);
            var editor = this.getAEditorById(editorId);
            if (editor) {
                var code = this.getCodeFromEditor(editor);
                snippet.content = code;
            }
        });

        // request for update
        var snippets = this.snippets;
        var updateArticleRequest = {
            title,
            snippets,
        };
        Client.updateArticle(articleId, updateArticleRequest).then((resp) => {
            this.$notify({
                title: 'success',
                message: 'Article Saved',
                type: 'success',
            });
        });
    }

    swapObjectsByIds(array, id1, id2) {
        // Find the indexes of the objects in the array
        const index1 = array.findIndex((obj) => obj.id == id1);
        const index2 = array.findIndex((obj) => obj.id == id2);

        // Swap the objects at the found indexes
        const temp = array[index1];
        Vue.set(array, index1, array[index2]);
        Vue.set(array, index2, temp);

        // Return the updated array
        return array;
    }

    upOrDownASnippet(upOrDown) {
        var snippetId = this.selectedSnippetId;
        var sib = this.findSiblineSnippet(snippetId, upOrDown);

        if (!sib) {
            return;
        }

        var sibId = sib.id;
        var snippetAId = snippetId;
        var snippetBId = sibId;

        Client.exchangeOrder({ snippetAId, snippetBId }).then((resp) => {
            // update the order of all snippets by snippet id according to orderMap
            var orderMap = resp.data.orderMap;
            this.updateOrder(orderMap);
        });
    }

    // find sibling snippet by id
    findSiblineSnippet(snippetId, direction) {
        switch (direction) {
            case 'next':
                return this.findNextSnippet(snippetId);
            case 'previous':
                return this.findPreviousSnippet(snippetId);
            default:
                throw Error();
        }
    }

    // find next or previous snippet
    findNextSnippet(snippetId) {
        var index = this.snippets.findIndex(
            (snippet) => snippet.id == snippetId,
        );

        // snippet is not in the array or array is empty
        if (index == -1) {
            return;
        }

        // last snippet, return the first one to cycle
        if (index == this.snippets.length - 1) {
            // return this.snippets[0]
            return;
        }

        return this.snippets[index + 1];
    }

    findPreviousSnippet(snippetId) {
        var index = this.snippets.findIndex(
            (snippet) => snippet.id == snippetId,
        );

        // snippet is not in the array or array is empty
        if (index == -1) {
            return;
        }

        // first snippet, return the last one to cycle
        if (index == 0) {
            // return this.snippets[this.snippets.length - 1]
            return;
        }

        return this.snippets[index - 1];
    }

    // remove the snippet seletedSnippetId points to
    removeSnippet() {
        // check if there is still any snippt
        if (this.snippets.length == 0) {
            return;
        }

        var snippetId = this.selectedSnippetId;

        // find out the index of selected snippet in the array
        var originalPlace = this.snippets.findIndex(
            (snippet) => snippet.id == snippetId,
        );

        var isLast = false;

        // determine if this snippet is the last
        if (this.snippets.length > 0) {
            var lastSnippet = this.snippets[this.snippets.length - 1];
            if (lastSnippet.id == snippetId) {
                isLast = true;
            }
        }

        Client.deleteSnippet(snippetId).then((res) => {
            // remove the snippet from the snippets array
            this.snippets = this.snippets.filter(
                (snippet) => snippet.id != snippetId,
            );

            // if there is no other snippet, over
            if (this.snippets.length == 0) {
                this.focusOnNothing();
                return;
            }

            var orderMap = res.data.orderMap;
            this.updateOrder(orderMap);

            this.$nextTick(function () {
                // focus the same place if we just removed the last one snippet
                if (isLast) {
                    // focus on the last one
                    this.focusSnippetBySnippetId(
                        this.snippets[this.snippets.length - 1].id,
                    );
                } else {
                    // focus the next snippet if we just removed the snippet that is not the last one
                    this.focusSnippetBySnippetId(
                        this.snippets[originalPlace].id,
                    );
                }

                // remove editor
                var editorId = this.formAnIdForEditorDiv({ id: snippetId });
                this.removeAnEditor(editorId);
            });
        });
    }

    // update order of elems according to the orderMap
    updateOrder(orderMap) {
        this.snippets.forEach((snippet) => {
            if (orderMap[snippet.id] != undefined) {
                snippet.order = orderMap[snippet.id];
            }
        });

        // sort snippets by order
        this.snippets.sort((a, b) => {
            return a.order - b.order;
        });

        this.$nextTick(function () {
            // reload all richtext
            this.snippets.forEach((snippet) => {
                // find the richtext div
                var richTextEditorId = this.formAnIdForRichTextEditor(snippet);
                var richText = this.getARichTextById(richTextEditorId);

                // reload the richtext
                richText.reload();
            });
        });

        // this.renderDescToRichText()
    }

    getAllSnippetElement() {
        // get all elements whose id starts with "snippet"
        const snippetElements = document.querySelectorAll(
            this.snippetDivSelector(),
        );
        return snippetElements;
    }

    getSippetIdFromDivId(divId) {
        return divId.substring(divId.indexOf('snippet-') + 'snippet-'.length);
    }

    formAnIdForSnippetDiv(snippet) {
        return `snippet-${snippet.id}`;
    }

    snippetDivSelector() {
        return '[id^="snippet-"]';
    }

    focusSnippet(e) {
        // get snippet id from element, use closest to ensure if you click the nested elements like input or something, you can still get
        // the "snippet-" prefixed div correctly
        var target = e.target.closest(this.snippetDivSelector());

        var selectedSnippetId = this.getSippetIdFromDivId(target.id);

        this.doFocusSnippet(target, selectedSnippetId);
    }

    focusOnNothing() {
        this.selectedSnippetId = -1;
    }

    focusSnippetBySnippetId(selectedSnippetId) {
        // get snippet element by id
        var target = document.getElementById(
            this.formAnIdForSnippetDiv({ id: selectedSnippetId }),
        );
        this.doFocusSnippet(target, selectedSnippetId);
    }

    doFocusSnippet(target, selectedSnippetId) {
        // highlight it
        target.classList.add('snippet-highlight');

        this.selectedSnippetId = selectedSnippetId;

        // remove highlight from the rest snippet
        this.reverseHighLightSnippet(selectedSnippetId);
    }

    reverseHighLightSnippet(selectedSnippetId) {
        // remove highlight from the rest snippet
        var snippetElements = this.getAllSnippetElement();
        snippetElements.forEach((element) => {
            // get the part of the ID that follows "snippet"
            const snippetId = this.getSippetIdFromDivId(element.id);
            if (snippetId != selectedSnippetId) {
                console.log(snippetId); // output: "123"
                element.classList.remove('snippet-highlight');
            }
        });
    }

    addNewSnippet() {
        // everytime a new snippet is added to the view, a new TinyMCE component will be rendered to the view(created),
        // so the page should be loading before the component is created,
        // this variable will be set to false in the callback of the component's inited hook
        // and the page will be prepared to use(loading over)
        this.tinyMceCompnentLoading = true;

        // find order of current focused snippet
        var snippets = this.snippets;
        var selectedOrder = -1;
        snippets.forEach((snippet) => {
            if (snippet.id == this.selectedSnippetId) {
                selectedOrder = snippet.order;
            }
        });

        // we are going to create the first one snippet
        var newOrder = selectedOrder < 0 ? 0 : selectedOrder + 1;

        Client.createSnippet(this.articleId, {
            content: '',
            description: '',
            lang: 'kotlin',
            order: newOrder,
        }).then((resp) => {
            var newSnippetHandle = resp.data.newSnippetHandle;

            // add new snippet to snippets anyway
            var newSnippet = {
                id: newSnippetHandle,
                content: '',
                description: '',
                lang: 'kotlin',
                order: newOrder,
            };
            this.snippets.push(newSnippet);

            // update the order of all snippets by snippet id according to orderMap
            var orderMap = resp.data.orderMap;
            this.updateOrder(orderMap);

            // focus on the new snippet
            this.$nextTick(function () {
                this.focusSnippetBySnippetId(newSnippetHandle);
                this.renderMonaco(newSnippet);
            });
        });
    }

    handleKeyDown(event) {
        // Check if the "Ctrl" key and "S" key were both pressed
        if (event.ctrlKey && event.key === 's') {
            event.preventDefault();
            console.log('Ctrl + S pressed!');
            this.saveArticle();
            // Do something else here, such as saving data or triggering an action
        }
    }

    handleOptionChange(snippet) {
        this.updateLanguageForSnippet(snippet.id, snippet.lang);
    }
    runCode() {
        //
    }

    init() {
        // get article id from url
        Client.getArticle(this.articleId)
            .then((resp) => {
                this.title = resp.data.title;
                return Client.listSnippetsForArticle(this.articleId);
            })
            .then((resp) => {
                this.snippets = resp.data;
                this.snippets.sort((a, b) => a.order - b.order);

                if (this.snippets.length > 0) {
                    this.$nextTick(function () {
                        // focus the first snippet
                        this.snippets.forEach((snippet) => {
                            this.renderMonaco(snippet);
                        });
                        // render editor to ui
                        this.focusSnippetBySnippetId(this.snippets[0].id);
                    });
                }

                this.dataLoading = false;

                if (this.snippets.length <= 0) {
                    this.tinyMceCompnentLoading = false;
                }
            });
    }

    updateLanguageForSnippet(snippetId, lang) {
        // request backend to change language
        Client.updateLanguageForSnippet(snippetId, lang).then((resp) => {
            this.$nextTick(function () {
                // find the snippet
                var snippet = this.snippets.find(
                    (snippet) => snippet.id == snippetId,
                );
                this.renderMonaco(snippet);
            });
        });
    }
}
</script>

<style lang="scss" scoped>
@import '~@/style/common-style';

.snippet-highlight {
    background-color: azure;
}
</style>
