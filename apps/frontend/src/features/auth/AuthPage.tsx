import { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { getRouteApi } from "@tanstack/react-router";
import {
  useLogin,
  useRegister,
  loginRequestDtoSchema,
  registerRequestDtoSchema
} from "@camper-rent/api-client";
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  CircularProgress,
  Container,
  Stack,
  Tab,
  Tabs,
  TextField,
  Typography
} from "@mui/material";

import { env } from "../../utils/env";
import { useAuthStore } from "../../store/auth-store";

const authRouteApi = getRouteApi("/auth");

type FormValues = {
  email: string;
  password: string;
};

export function AuthPage() {
  const { t, i18n } = useTranslation();
  const { redirectTo } = authRouteApi.useSearch();
  const navigate = authRouteApi.useNavigate();
  const [tab, setTab] = useState<"login" | "register">("login");
  const auth = useAuthStore((state) => state.auth);
  const setAuth = useAuthStore((state) => state.setAuth);
  const clearAuth = useAuthStore((state) => state.clearAuth);

  function goAfterAuth() {
    navigate({ to: redirectTo ?? "/", replace: true });
  }

  const formSchema = useMemo(() => {
    const emailValidated = z.email({ message: t("validation.emailInvalid") });
    const passwordValidated = z.string().min(8, { message: t("validation.passwordMin") });

    if (tab === "register") {
      return registerRequestDtoSchema.extend({
        email: emailValidated,
        password: passwordValidated.max(2147483647)
      });
    }

    return loginRequestDtoSchema.extend({
      email: emailValidated,
      password: passwordValidated
    });
  }, [t, tab]);

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
      password: ""
    }
  });

  const loginMutation = useLogin({
    mutation: {
      onSuccess: (response) => {
        setAuth(response);
        goAfterAuth();
      }
    },
    client: {
      baseURL: env.apiBaseUrl
    }
  });

  const registerMutation = useRegister({
    mutation: {
      onSuccess: (response) => {
        setAuth(response);
        goAfterAuth();
      }
    },
    client: {
      baseURL: env.apiBaseUrl
    }
  });

  const isLoading = loginMutation.isPending || registerMutation.isPending;
  const errorMessage = loginMutation.error?.message ?? registerMutation.error?.message ?? null;

  function onSubmit(values: FormValues) {
    if (tab === "login") {
      loginMutation.mutate({ data: values });
      return;
    }
    registerMutation.mutate({ data: values });
  }

  return (
    <Container maxWidth="sm" sx={{ py: 6 }} key={i18n.language}>
      <Stack spacing={3}>
        <Typography variant="h4" fontWeight={700}>
          {t("auth.title")}
        </Typography>

        <Card>
          <CardContent>
            <Tabs value={tab} onChange={(_, value: "login" | "register") => setTab(value)} sx={{ mb: 2 }}>
              <Tab value="login" label={t("auth.tabLogin")} />
              <Tab value="register" label={t("auth.tabRegister")} />
            </Tabs>

            <Box component="form" onSubmit={form.handleSubmit(onSubmit)}>
              <Stack spacing={2}>
                <TextField
                  label={t("auth.email")}
                  type="email"
                  fullWidth
                  {...form.register("email")}
                  error={!!form.formState.errors.email}
                  helperText={form.formState.errors.email?.message}
                />
                <TextField
                  label={t("auth.password")}
                  type="password"
                  fullWidth
                  {...form.register("password")}
                  error={!!form.formState.errors.password}
                  helperText={form.formState.errors.password?.message}
                />

                {errorMessage && <Alert severity="error">{errorMessage}</Alert>}

                <Button type="submit" variant="contained" disabled={isLoading}>
                  {isLoading ? (
                    <CircularProgress size={20} color="inherit" />
                  ) : tab === "login" ? (
                    t("auth.submitLogin")
                  ) : (
                    t("auth.submitRegister")
                  )}
                </Button>
              </Stack>
            </Box>
          </CardContent>
        </Card>

        <Card>
          <CardContent>
            <Stack spacing={1.5}>
              <Typography variant="h6">{t("auth.currentUser")}</Typography>
              {!auth && <Typography color="text.secondary">{t("auth.noSession")}</Typography>}
              {auth && (
                <>
                  <Typography>
                    <strong>{t("auth.publicId")}:</strong> {auth.publicId}
                  </Typography>
                  <Typography>
                    <strong>{t("auth.emailLabel")}:</strong> {auth.email}
                  </Typography>
                  <Stack direction="row" spacing={1} flexWrap="wrap">
                    {(auth.roles ?? []).map((role) => (
                      <Chip key={role} label={role} size="small" color="primary" variant="outlined" />
                    ))}
                  </Stack>
                  <Button
                    variant="outlined"
                    color="secondary"
                    onClick={() => {
                      clearAuth();
                      navigate({ to: "/auth", replace: true });
                    }}
                    sx={{ alignSelf: "flex-start" }}
                  >
                    {t("auth.logout")}
                  </Button>
                </>
              )}
            </Stack>
          </CardContent>
        </Card>
      </Stack>
    </Container>
  );
}
