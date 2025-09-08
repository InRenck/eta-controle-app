// functions/index.js
const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.enviarNotificacaoNovoPedido = functions.firestore
    .document("vendas/{vendaId}")
    .onCreate(async (snapshot, context) => {
      const novoPedido = snapshot.data();
      const clienteNome = novoPedido.cliente;

      const payload = {
        notification: {
          title: "Novo Pedido Recebido!",
          body: `Um novo pedido de ${clienteNome} foi cadastrado.`,
        },
      };

      try {
        const tokensSnapshot = await admin.firestore().collection("fcmTokens").get();
        const tokens = tokensSnapshot.docs.map(doc => doc.data().token);

        if (tokens.length > 0) {
          const response = await admin.messaging().sendToDevice(tokens, payload);
          console.log("Notificação enviada com sucesso:", response);
        } else {
          console.log("Nenhum token de dispositivo encontrado.");
        }

      } catch (error) {
        console.error("Erro ao enviar a notificação:", error);
      }
    });