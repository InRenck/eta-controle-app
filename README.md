# ETA Controle App

Aplicativo de controle de pedidos, produtos e estoque desenvolvido em Android Studio com Kotlin.

## 📱 Funcionalidades

- 📦 Cadastro e visualização de produtos
- 🧾 Criação e gerenciamento de pedidos
- 🗃 Controle de estoque
- 🔍 Barra de busca em todas as seções
- 📊 Navegação entre as abas: Vendas, Estoque, Produtos e Pedidos
- 👥 Tela de login e cadastro de usuário

## 🎨 Design

A interface foi baseada em um protótipo no [Figma](https://www.figma.com/design/pBqK17c25i306JBwwFXJR0/Untitled?node-id=0-1&m=dev).

## 🛠 Tecnologias

- Kotlin
- Android XML Layout
- RecyclerView
- CardView
- Fragments e Bottom Navigation

## 🚧 Em desenvolvimento

Este projeto ainda está em fase de desenvolvimento. Novas funcionalidades estão sendo adicionadas continuamente.

## 📁 Estrutura

app/
├── activities/ # Splash, Login e Signup
├── fragments/ # Vendas, Estoque, Produtos, Pedidos
├── adapters/ # Adapters de RecyclerView
├── models/ # Classe Produto e outros modelos
├── res/layout/ # Arquivos XML dos layouts
└── MainActivity.kt # Atividade principal com Bottom Navigation
