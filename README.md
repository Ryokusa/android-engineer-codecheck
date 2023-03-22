# 株式会社ゆめみ Android エンジニアコードチェック課題

## 概要

[株式会社ゆめみ Android エンジニアコードチェック課題](https://github.com/yumemi-inc/android-engineer-codecheck)のコピーリポジトリ

## アプリ仕様

本アプリは GitHub のリポジトリを検索するアプリです。

<img src="docs/app.gif" width="320">

### 環境

- IDE：Android Studio Electric Eel | 2022.1.1 Patch 2
- Kotlin：1.8.0
- Java：11
- Gradle：7.4.2
- minSdk：23
- targetSdk：33

※ ライブラリの利用はオープンソースのものに限ります。

### 動作

1. 何かしらのキーワードを入力
2. GitHub API（`search/repositories`）でリポジトリを検索し、結果一覧を概要（リポジトリ名）で表示
3. 特定の結果を選択したら、該当リポジトリの詳細（リポジトリ名、オーナーアイコン、プロジェクト言語、Star 数、Watcher 数、Fork 数、Issue 数）を表示

## 開発時の注意事項

### gitフローについて

[GitFlow](https://docs.github.com/ja/get-started/quickstart/github-flow)に準拠

### コミットについて

[ConvertionalCommits](https://www.conventionalcommits.org/ja/v1.0.0/)に準拠  
