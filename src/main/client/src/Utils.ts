export const encodeQuery = (params: Record<string, string>) => {
    return new URLSearchParams(params).toString();
}