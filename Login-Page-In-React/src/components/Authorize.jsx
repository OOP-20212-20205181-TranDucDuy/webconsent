import React, { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { Box, Button, Card, CardContent, Typography, List, ListItem } from "@mui/material";
import { baseUrl } from "./Login";
const Authorize = () => {
  const [accessToken, setAccessToken] = useState("");
  const [authStatus, setAuthStatus] = useState(null);
  const [clientId, setClientId] = useState("");
  const [clientSecret, setClientSecret] = useState("");
  const [path, setPath] = useState("");
  const [provisionKey, setProvisionKey] = useState("");
  const [authorizationUserid, setAuthorizationUserid] = useState("");

  useEffect(() => {
    setAccessToken(localStorage.getItem("accessToken") || "");
    setClientId(localStorage.getItem("clientId") || "");
    setClientSecret(localStorage.getItem("clientSecret") || "");
    setAuthorizationUserid(localStorage.getItem("authorizationUserid") || "");
    setProvisionKey(localStorage.getItem("provisionKey") || "");
    setPath(localStorage.getItem("path") || "");
  }, []);

  const handleAuthorize = async () => {
    try {
      const response = await fetch(`${baseUrl}/oauth2/authorize`, {
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
      setAuthStatus(`✅ Authorization successful!Access Token: ${data.accessToken}`);
    } catch (err) {
      setAuthStatus(`❌ Error: ${err.message}`);
    }
  };

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh" bgcolor="#f4f4f4" p={3}>
      <Card sx={{ maxWidth: 400, width: "100%", boxShadow: 3, borderRadius: 3, p: 3 }}>
        <motion.div initial={{ opacity: 0, scale: 0.9 }} animate={{ opacity: 1, scale: 1 }} transition={{ duration: 0.3 }}>
          <Box bgcolor="#1976d2" py={2} textAlign="center" borderRadius="3px 3px 0 0">
          </Box>
          <CardContent>
            <Typography variant="h5" gutterBottom textAlign="center" fontWeight={600} color="primary">
              Authorization
            </Typography>
            <Typography variant="body1" color="textSecondary" textAlign="center" mb={2}>
              This application requires access to your account.
            </Typography>
            <List>
              <ListItem sx={{ fontSize: "1rem", fontWeight: 500, color: "#444" }}>✅ Read and write all files and folders stored in Box</ListItem>
            </List>
            <Button fullWidth variant="contained" color="primary" onClick={handleAuthorize} sx={{ mt: 2, fontSize: "1rem", fontWeight: 600 }}>
              Grant Access
            </Button>
            <Button fullWidth variant="text" color="secondary" sx={{ mt: 1, fontSize: "1rem", fontWeight: 600 }}>
              Deny Access
            </Button>
            {authStatus && <Typography mt={2} variant="body1" color="error" textAlign="center" fontWeight={500}>{authStatus}</Typography>}
          </CardContent>
        </motion.div>
      </Card>
    </Box>
  );
};

export default Authorize;