import request from '@/request';
import { AxiosPromise } from 'axios';

class Client {
    // region: Playground Project Template
    static createPlaygroundProjectTemplateFromDirectory(
        path: string,
        name: string,
    ): AxiosPromise<any> {
        return request({
            url: `/playground-project-template/from-directory`,
            method: 'post',
            data: {
                path,
                name,
            },
        });
    }

    static uploadPlaygroundProjectTemplate(
        formData: FormData,
    ): AxiosPromise<any> {
        return request({
            url: `/playground-project-template`,
            method: 'post',
            data: formData,
        });
    }

    static updatePlaygroundProjectTemplateBinary(
        playgroundProjectTemplateId: number,
        formData: FormData,
    ): AxiosPromise<any> {
        return request({
            url: `/playground-project-template-binary/${playgroundProjectTemplateId}`,
            method: 'put',
            data: formData,
        });
    }

    static deletePlaygroundProjectTemplate(
        playgroundProjectTemplateId: number,
    ): AxiosPromise<any> {
        return request({
            url: `/playground-project-template/${playgroundProjectTemplateId}`,
            method: 'delete',
        });
    }

    static getPlaygroundProjectTemplateList(): AxiosPromise<any> {
        return request({
            url: `/playground-project-templates`,
            method: 'get',
        });
    }

    static downloadPlaygroundProjectTemplate(
        playgroundProjectTemplateId: number,
    ): AxiosPromise<any> {
        return request({
            url: `/playground-project-template/${playgroundProjectTemplateId}`,
            method: 'get',
            params: { playgroundProjectTemplateId },
            responseType: 'blob',
        });
    }

    // endregion

    // region: article

    static createArticle(createArticleRequest: any) {
        return request({
            url: '/article',
            method: 'post',
            data: createArticleRequest,
        });
    }

    static deleteArticle(articleId: any) {
        return request({
            url: `/article/${articleId}`,
            method: 'delete',
        });
    }

    static updateArticle(articleId: any, updateArticleRequest: any) {
        return request({
            url: `/article/${articleId}`,
            method: 'put',
            data: updateArticleRequest,
        });
    }

    static listArticles(queryArticle: any) {
        return request({
            url: '/articles',
            method: 'get',
            params: queryArticle,
        });
    }

    static getArticle(articleId: any) {
        return request({
            url: `/article/${articleId}`,
            method: 'get',
        });
    }

    static listSnippetsForArticle(articleId: any) {
        return request({
            url: `/article/snippets/${articleId}`,
            method: 'get',
        });
    }

    static createSnippet(articleId: any, createArticleRequest: any) {
        return request({
            url: `/article/${articleId}/snippet`,
            method: 'post',
            data: createArticleRequest,
        });
    }

    static deleteSnippet(snippetId: any) {
        return request({
            url: `/article/snippet/${snippetId}`,
            method: 'delete',
        });
    }

    // endregion

    //     region: config

    static updateConfig(key: string, value: string) {
        return request({
            url: '/config',
            method: 'put',
            params: {
                key,
                value,
            },
        });
    }

    static listConfig() {
        return request({
            url: '/config/list',
            method: 'get',
        });
    }

    static getConfigValue(key: any) {
        return request({
            url: '/config',
            method: 'get',
            params: { key },
        });
    }

    // endregion

    //     region: snippet
    static exchangeOrder(exchangeOrderRequest: any) {
        return request({
            url: '/snippet/order/exchange',
            method: 'put',
            data: exchangeOrderRequest,
        });
    }

    static updateLanguageForSnippet(snippetId: any, lang: any) {
        return request({
            url: `/snippet/${snippetId}`,
            method: 'put',
            data: { lang },
        });
    }

    // endregion
}

export default Client;
