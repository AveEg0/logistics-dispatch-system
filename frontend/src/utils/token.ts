let inMemoryAccessToken: string | null = null;

export const setAccessToken = (token: string): void => {
  inMemoryAccessToken = token;
};

export const getAccessToken = (): string | null => inMemoryAccessToken;

export const clearAccessToken = (): void => {
  inMemoryAccessToken = null;
};
