import React, { useEffect, useState } from "react";
import { motion } from "framer-motion";
import {
  Box,
  Button,
  Card,
  CardContent,
  Typography,
  List,
  ListItem,
  Divider,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";
import { BASE_URL } from "./Login";

const Authorize = () => {
  const [accessToken, setAccessToken] = useState("");
  const [authStatus, setAuthStatus] = useState(null);
  const [clientId, setClientId] = useState("");
  const [clientSecret, setClientSecret] = useState("");
  const [path, setPath] = useState("");
  const [provisionKey, setProvisionKey] = useState("");
  const [authorizationUserid, setAuthorizationUserid] = useState("");
  const [logs, setLogs] = useState([]);

  const [dialogOpen, setDialogOpen] = useState(false);
  const [recentToken, setRecentToken] = useState("");

  useEffect(() => {
    setAccessToken(localStorage.getItem("accessToken") || "");
    setClientId(localStorage.getItem("clientId") || "");
    setClientSecret(localStorage.getItem("clientSecret") || "");
    setAuthorizationUserid(localStorage.getItem("authorizationUserid") || "");
    setProvisionKey(localStorage.getItem("provisionKey") || "");
    setPath(localStorage.getItem("path") || "");

    // Lấy log token từ localStorage nếu có
    const storedLogs = localStorage.getItem("oauthTokenLogDtos");
    if (storedLogs) {
      try {
        setLogs(JSON.parse(storedLogs));
      } catch (err) {
        console.error("❌ Error parsing logs:", err);
      }
    }
  }, []);

  const handleAuthorize = async () => {
    try {
      const response = await fetch(`${BASE_URL}/oauth2/authorize`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
        body: JSON.stringify({
          clientId,
          clientSecret,
          provisionKey,
          authorizationUserid,
          path,
        }),
      });

      if (!response.ok) throw new Error("Authorization failed");
      const data = await response.json();
      setAuthStatus(`✅ Authorization successful!`);
      setRecentToken(data.accessToken); // lưu token để show dialog
      setDialogOpen(true); // mở popup
    } catch (err) {
      setAuthStatus(`❌ Error: ${err.message}`);
    }
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
  };

  const formatDate = (timestamp) => {
    const date = new Date(timestamp * 1000);
    return date.toLocaleString();
  };

  return (
    <Box display="flex" flexDirection="column" alignItems="center" p={3} bgcolor="#f4f4f4" minHeight="100vh">
      <Card sx={{ maxWidth: 500, width: "100%", boxShadow: 3, borderRadius: 3, p: 3 }}>
        <motion.div initial={{ opacity: 0, scale: 0.9 }} animate={{ opacity: 1, scale: 1 }} transition={{ duration: 0.3 }}>
          <CardContent>
            <Typography variant="h5" gutterBottom textAlign="center" fontWeight={600} color="primary">
              Authorization
            </Typography>
            <Typography variant="body1" color="textSecondary" textAlign="center" mb={2}>
              This application requires access to your account.
            </Typography>
            <List>
              <ListItem sx={{ fontSize: "1rem", fontWeight: 500, color: "#444" }}>
                ✅ Read and write all files and folders stored in Box
              </ListItem>
            </List>
            <Button fullWidth variant="contained" color="primary" onClick={handleAuthorize} sx={{ mt: 2, fontSize: "1rem", fontWeight: 600 }}>
              Grant Access
            </Button>
            <Button fullWidth variant="text" color="secondary" sx={{ mt: 1, fontSize: "1rem", fontWeight: 600 }}>
              Deny Access
            </Button>
            {authStatus && (
              <Typography mt={2} variant="body1" color="error" textAlign="center" fontWeight={500}>
                {authStatus}
              </Typography>
            )}
          </CardContent>
        </motion.div>
      </Card>

      {/* Authorization Logs */}
      {logs.length > 0 && (
        <Card sx={{ maxWidth: 600, width: "100%", mt: 4, boxShadow: 2, borderRadius: 3 }}>
          <CardContent>
            <Typography variant="h6" gutterBottom color="primary">
              📝 Authorization Log History
            </Typography>
            {logs.map((log) => (
              <Box key={log.id} mb={2} p={2} border="1px solid #ccc" borderRadius={2}>
                <Typography variant="body2">🔑 Token: {log.accessToken}</Typography>
                <Typography variant="body2">📅 Created At: {formatDate(log.createdAt)}</Typography>
                <Typography variant="body2">🔐 Scope: {log.scope || "N/A"}</Typography>
                <Typography variant="body2">⌛ Expires In: {log.expiresIn} seconds</Typography>
              </Box>
            ))}
          </CardContent>
        </Card>
      )}

      {/* 🎉 Dialog Popup */}
      <Dialog open={dialogOpen} onClose={handleCloseDialog}>
        <DialogTitle>🎉 Authorization Successful!</DialogTitle>
        <DialogContent>
          <Typography variant="body1" mb={1}>
            Access Token vừa cấp là:
          </Typography>
          <Box
            sx={{
              p: 2,
              backgroundColor: "#f0f0f0",
              borderRadius: 2,
              fontWeight: "bold",
              color: "#1976d2",
              border: "2px dashed #1976d2",
              wordBreak: "break-word",
            }}
          >
            {recentToken}
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} color="primary" variant="contained">
            OK
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default Authorize;
