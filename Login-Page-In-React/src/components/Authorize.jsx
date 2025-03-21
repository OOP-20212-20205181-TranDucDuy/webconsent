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
      setAuthStatus("✅ Authorization successful!");
      setRecentToken(data.accessToken);
      setDialogOpen(true);
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
    <Box display="flex" flexDirection="column" alignItems="center" p={4} bgcolor="#f4f4f4" minHeight="100vh">
      <Card sx={{ maxWidth: 600, width: "100%", boxShadow: 4, borderRadius: 4, p: 4 }}>
        <motion.div initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }} transition={{ duration: 0.4 }}>
          <CardContent>
            <Typography variant="h4" gutterBottom textAlign="center" fontWeight={700} color="primary">
              Authorization
            </Typography>
            <Typography variant="body1" color="textSecondary" textAlign="center" mb={3} fontSize="1.2rem">
              This application requires access to your account.
            </Typography>
            <List>
              <ListItem sx={{ fontSize: "1.1rem", fontWeight: 600, color: "#444", py: 2 }}>
                ✅ Read and write all files and folders stored in Box
              </ListItem>
            </List>
            <Button
              fullWidth
              variant="contained"
              color="primary"
              onClick={handleAuthorize}
              sx={{ mt: 3, fontSize: "1.1rem", fontWeight: 700, py: 1.5, borderRadius: 2 }}
            >
              Grant Access
            </Button>
            <Button
              fullWidth
              variant="text"
              color="secondary"
              sx={{ mt: 2, fontSize: "1.1rem", fontWeight: 700, py: 1 }}
            >
              Deny Access
            </Button>
            {authStatus && (
              <Typography mt={3} variant="body1" color="error" textAlign="center" fontWeight={600} fontSize="1.1rem">
                {authStatus}
              </Typography>
            )}
          </CardContent>
        </motion.div>
      </Card>

      {/* Authorization Logs */}
      {logs.length > 0 && (
        <Card sx={{ maxWidth: 700, width: "100%", mt: 4, boxShadow: 2, borderRadius: 3 }}>
          <CardContent>
            <Typography variant="h6" gutterBottom color="primary" fontSize="1.3rem">
              📝 Authorization Log History
            </Typography>
            {logs.map((log) => (
              <Box key={log.id} mb={2} p={2.5} border="1px solid #ccc" borderRadius={2} sx={{ fontSize: "1.05rem" }}>
                <Typography variant="body2" mb={1}>🔑 Token: {log.accessToken}</Typography>
                <Typography variant="body2" mb={1}>📅 Created At: {formatDate(log.createdAt)}</Typography>
                <Typography variant="body2" mb={1}>🔐 Scope: {log.scope || "N/A"}</Typography>
                <Typography variant="body2">⌛ Expires In: {log.expiresIn} seconds</Typography>
              </Box>
            ))}
          </CardContent>
        </Card>
      )}

      {/* Dialog Popup */}
      <Dialog open={dialogOpen} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle sx={{ fontSize: "1.5rem", fontWeight: 700 }}>🎉 Authorization Successful!</DialogTitle>
        <DialogContent>
          <Typography variant="body1" mb={2} fontSize="1.2rem">
            Access Token vừa cấp là:
          </Typography>
          <Box
            sx={{
              p: 3,
              backgroundColor: "#f0f0f0",
              borderRadius: 3,
              fontWeight: "bold",
              fontSize: "1.2rem",
              color: "#1976d2",
              border: "2px dashed #1976d2",
              wordBreak: "break-word",
            }}
          >
            {recentToken}
          </Box>
        </DialogContent>
        <DialogActions sx={{ p: 2 }}>
          <Button onClick={handleCloseDialog} color="primary" variant="contained" sx={{ fontSize: "1rem", fontWeight: 600 }}>
            OK
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default Authorize;
